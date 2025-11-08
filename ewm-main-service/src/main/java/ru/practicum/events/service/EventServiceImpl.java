package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.client.StatClient;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.dto.ResponseStatDto;
import ru.practicum.events.dto.*;
import ru.practicum.events.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.events.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.mapping.EventMapper;
import ru.practicum.events.mapping.RequestMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.Request;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.repository.LocationRepository;
import ru.practicum.events.repository.RequestRepository;
import ru.practicum.events.status.EventRequestStatus;
import ru.practicum.events.status.EventSort;
import ru.practicum.events.status.EventState;
import ru.practicum.events.status.StateAction;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.practicum.events.model.Event.timeFormat;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final StatClient statClient;

    @Override
    public List<EventShortDto> search(SearchDto searchDto,
                                      HttpServletRequest httpServletRequest) {
        List<Event> events = search(searchDto.getRangeStart(), searchDto.getRangeEnd());

        // Вероятно, все эти проверки можно было засунуть в репозиторий,
        // но, думаю, вышло бы больше методов, чем если использовать много стримов ㅠㅠ
        if (searchDto.getText() != null && !searchDto.getText().isBlank()) {
            String text = searchDto.getText().toLowerCase();
            String finalText = text;
            events = events.stream().filter(e -> e.getAnnotation().toLowerCase().contains(finalText)
                            || e.getTitle().toLowerCase().contains(finalText)
                            || e.getDescription().toLowerCase().contains(finalText))
                    .toList();
        }
        if (searchDto.getCategories() != null && !searchDto.getCategories().isEmpty()) {
            events = filterCategories(events, searchDto.getCategories());
        }
        if (searchDto.getPaid() != null) {
            events = events.stream()
                    .filter(e -> e.isPaid() == searchDto.getPaid())
                    .toList();
        }
        if (searchDto.getOnlyAvailable()) {
            events = events.stream()
                    .filter(e -> e.getParticipantAmount() < e.getParticipantLimit())
                    .toList();
        }

        List<EventShortDto> eventShortDtos = events.stream().map(EventMapper::toShortDto).toList();
        eventShortDtos = setViews(eventShortDtos);

        EventSort sortEnum;
        try {
            sortEnum = EventSort.valueOf(searchDto.getSort().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Некорректная сортировка. " +
                    "События можно сортировать по дате или по количеству просмотров");
        }
        if (sortEnum.equals(EventSort.EVENT_DATE)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .toList();
        } else if (sortEnum.equals(EventSort.VIEWS)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .toList();
        } else if (sortEnum.equals(EventSort.ID)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getId))
                    .toList();
        }

        List<EventShortDto> searchList = new ArrayList<>();
        if (!events.isEmpty()) {
            for (int i = searchDto.getFrom(); i < searchDto.getFrom() + searchDto.getSize() && i < events.size(); i++) {
                searchList.add(EventMapper.toShortDto(events.get(i)));
            }
        }
        statClient.saveStat(httpServletRequest);
        return searchList;
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = searchEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Событие с ID " + eventId + " недоступно");
        }
        statClient.saveStat(httpServletRequest);
        Event saved = eventRepository.save(event);
        return toFullWithViewsAndComments(saved);
    }

    @Override
    public List<EventFullDto> adminSearch(SearchDto searchDto) {
        List<Event> events = search(searchDto.getRangeStart(), searchDto.getRangeEnd());

        if (searchDto.getUsers() != null && !searchDto.getUsers().isEmpty()) {
            events = events.stream()
                    .filter(e -> searchDto.getUsers().contains(e.getInitiator().getId()))
                    .toList();
        }
        if (searchDto.getStates() != null && !searchDto.getStates().isEmpty()) {
            List<EventState> statesEnum = searchDto.getStates().stream()
                    .map(String::toUpperCase).map(EventState::valueOf).toList();
            events = events.stream()
                    .filter(e -> statesEnum.contains(e.getState()))
                    .toList();
        }
        if (searchDto.getCategories() != null && !searchDto.getCategories().isEmpty()) {
            events = filterCategories(events, searchDto.getCategories());
        }

        List<EventFullDto> searchList = new ArrayList<>();
        if (!events.isEmpty()) {
            List<String> eventIdStrings = new ArrayList<>();
            List<Long> eventIds = new ArrayList<>();
            for (int i = searchDto.getFrom(); i < searchDto.getFrom() + searchDto.getSize() && i < events.size(); i++) {
                eventIdStrings.add("/events/" + events.get(i).getId());
                eventIds.add(events.get(i).getId());
            }
            List<ResponseStatDto> views = statClient.getViewStats(
                    LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0), LocalDateTime.now(),
                    eventIdStrings, true);
            List<Comment> comments = commentRepository.findByEventIdIn(eventIds);
            for (int i = searchDto.getFrom(); i < searchDto.getFrom() + searchDto.getSize() && i < events.size(); i++) {
                Event event = events.get(i);
                List<ResponseStatDto> eventViews = new ArrayList<>();
                for (ResponseStatDto responseStatDto : views) {
                    if (responseStatDto.getApp().equals("/events/" + event.getId())) eventViews.add(responseStatDto);
                }
                List<Comment> eventComments = new ArrayList<>();
                for (Comment comm : comments) {
                    if (comm.getEvent().getId().equals(event.getId())) eventComments.add(comm);
                }
                EventFullDto fullDto = EventMapper.toFullDto(event);
                fullDto.setViews(eventViews.size());
                fullDto.setComments(eventComments.size());
                searchList.add(fullDto);
            }
        }
        return searchList;
    }

    @Override
    public EventFullDto adminEventUpdate(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = searchEvent(eventId);

        if (updateRequest.getAnnotation() != null && !updateRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) event.setCategory(searchCategory(updateRequest.getCategory()));
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().isBlank()) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null && !updateRequest.getEventDate().isBlank()) {
            event.setEventDate(checkEventDate(updateRequest.getEventDate(), event.getPublished()));
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(checkLocation(updateRequest.getLocation()));
        }
        if (updateRequest.getPaid() != null) event.setPaid(updateRequest.getPaid());
        if (updateRequest.getParticipantLimit() != null) event.setParticipantLimit(updateRequest.getParticipantLimit());
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getStateAction() != null && !updateRequest.getStateAction().isBlank()) {
            StateAction stateAction = StateAction.valueOf(updateRequest.getStateAction().toUpperCase());
            if (stateAction.equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(EventState.PENDING)) {
                    throw new ConflictException("Событие с ID " + eventId + " отменено или уже опубликовано");
                }
                event.setPublished(LocalDateTime.now());
                event.setState(EventState.PUBLISHED);
            }
            if (stateAction.equals(StateAction.REJECT_EVENT)) {
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new ConflictException("Нельзя отменить уже опубликованное событие");
                }
                event.setState(EventState.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isBlank()) {
            event.setTitle(updateRequest.getTitle());
        }
        Event saved = eventRepository.save(event);
        return toFullWithViewsAndComments(saved);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        checkUser(userId);
        Page<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from, size));
        List<EventShortDto> shorts = events.stream().map(EventMapper::toShortDto).toList();
        return setViews(shorts);
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User user = checkUser(userId);
        Location location = checkLocation(newEventDto.getLocation());
        Event event = EventMapper.toEvent(newEventDto, location);
        checkEventDate(event.getEventDate());

        if (newEventDto.getCategory() != null) {
            event.setCategory(searchCategory(newEventDto.getCategory()));
        }
        event.setInitiator(user);

        Event saved = eventRepository.save(event);
        return toFullWithViewsAndComments(saved);
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId, HttpServletRequest httpServletRequest) {
        Event event = searchEvent(eventId);
        checkInitiator(userId, event.getInitiator().getId(),
                "Просматривать полную информацию о событии может только его инициатор");

        Event saved = eventRepository.save(event);
        return toFullWithViewsAndComments(saved);
    }

    @Override
    public EventFullDto userEventUpdate(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = searchEvent(eventId);
        checkInitiator(userId, event.getInitiator().getId(),
                "Изменять событие может только его инициатор");
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Изменять можно только отмененные и ожидающие модерации события");
        }

        if (updateRequest.getAnnotation() != null && !updateRequest.getAnnotation().isBlank()) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            event.setCategory(searchCategory(updateRequest.getCategory()));
        }
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().isBlank()) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null && !updateRequest.getEventDate().isBlank()) {
            event.setEventDate(checkEventDate(updateRequest.getEventDate(), event.getPublished()));
            checkEventDate(event.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            event.setLocation(checkLocation(updateRequest.getLocation()));
        }
        if (updateRequest.getPaid() != null) event.setPaid(updateRequest.getPaid());
        if (updateRequest.getParticipantLimit() != null) event.setParticipantLimit(updateRequest.getParticipantLimit());
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getStateAction() != null && !updateRequest.getStateAction().isBlank()) {
            StateAction stateAction = StateAction.valueOf(updateRequest.getStateAction().toUpperCase());
            if (stateAction.equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
            if (stateAction.equals(StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isBlank()) {
            event.setTitle(updateRequest.getTitle());
        }

        Event saved = eventRepository.save(event);
        return toFullWithViewsAndComments(saved);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        Event event = searchEvent(eventId);
        checkInitiator(userId, event.getInitiator().getId(),
                "Просматривать заявки на участие в событии может только его инициатор");
        List<Request> requests = requestRepository.findByEventId(eventId);
        return requests.stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest request) {
        Event event = searchEvent(eventId);
        checkInitiator(userId, event.getInitiator().getId(),
                "Принимать и отклонять заявки на участие в событии может только его инициатор");
        EventRequestStatus newStatus = EventRequestStatus.valueOf(request.getStatus().toUpperCase());

        List<Request> eventRequest = requestRepository.findByIdIn(request.getRequestIds());
        int participantAmount = event.getParticipantAmount();
        ArrayList<String> errors = new ArrayList<>();
        for (Request req : eventRequest) {
            if (!req.getStatus().equals(EventRequestStatus.PENDING)) {
                throw new ConflictException("Можно изменять статус только у заявок, " +
                        "находящихся в режиме ожидания");
            }
            if (newStatus.equals(EventRequestStatus.CONFIRMED)
                    && event.getParticipantLimit() < participantAmount + 1) {
                newStatus = EventRequestStatus.REJECTED;
                String msg = "Заявка с ID " + req.getId() + " отклонена";
                errors.add(msg);
            }
            req.setStatus(newStatus);
        }

        List<Request> eventRequests = requestRepository.saveAll(eventRequest);
        List<ParticipationRequestDto> confirmedRequests = eventRequests.stream()
                .filter(r -> r.getStatus().equals(EventRequestStatus.CONFIRMED))
                .map(RequestMapper::toDto)
                .toList();
        List<ParticipationRequestDto> rejectedRequests = eventRequests.stream()
                .filter(r -> r.getStatus().equals(EventRequestStatus.REJECTED))
                .map(RequestMapper::toDto)
                .toList();
        if (!errors.isEmpty()) throw new ConflictException("Достигнут лимит участников для мероприятия");
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Event searchEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
    }

    private List<Event> search(String rangeStart, String rangeEnd) {
        LocalDateTime searchStart = null;
        LocalDateTime searchEnd = null;
        if (rangeStart == null && rangeEnd == null) {
            searchStart = LocalDateTime.now();
        }
        if (rangeStart != null) searchStart = LocalDateTime.parse(rangeStart, formatter);
        if (rangeEnd != null) searchEnd = LocalDateTime.parse(rangeEnd, formatter);


        List<Event> searchEvent;
        if (searchStart != null && searchEnd != null) {
            if (searchStart.isAfter(searchEnd) || searchStart.equals(searchEnd)) {
                throw new BadRequestException("Некорректно задано время");
            }
            searchEvent = eventRepository.findByEventDateIsAfterAndEventDateIsBefore(searchStart, searchEnd);
        } else if (searchStart != null) {
            searchEvent = eventRepository.findByEventDateIsAfter(searchStart);
        } else {
            searchEvent = eventRepository.findByEventDateIsBefore(searchEnd);
        }
        return searchEvent.stream().toList();
    }

    private Category searchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID " + categoryId + " не найдена"));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    private void checkInitiator(Long userId, Long initiatorId, String msg) {
        checkUser(userId);
        if (!initiatorId.equals(userId)) {
            throw new ForbiddenException(msg);
        }
    }

    private LocalDateTime checkEventDate(String eventDateString, LocalDateTime published) {
        LocalDateTime newEventDate = LocalDateTime.parse(eventDateString, formatter);
        if ((published != null && newEventDate.isBefore(published.minusHours(1)))
                || newEventDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Событие должно начинаться не раньше, чем за час до публикации");
        }
        return newEventDate;
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Событие должно начинаться не ранее, " +
                    "чем через два часа после создания");
        }
    }

    private Location checkLocation(LocationDto locationDto) {
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon())
                .orElseGet(() -> locationRepository.save(EventMapper.toLocation(locationDto)));
    }

    private List<Event> filterCategories(List<Event> events, List<Long> categories) {
        return events.stream()
                .filter(e -> e.getCategory() != null)
                .filter(e -> categories.contains(e.getCategory().getId()))
                .toList();
    }

    private List<EventShortDto> setViews(List<EventShortDto> eventShortDtos) {
        List<String> eventIds = new ArrayList<>();
        for (EventShortDto dto : eventShortDtos) {
            eventIds.add("/events/" + dto.getId());
        }
        List<ResponseStatDto> views = statClient.getViewStats(
                LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0), LocalDateTime.now(),
                eventIds, true);
        for (EventShortDto dto : eventShortDtos) {
            List<ResponseStatDto> eventViews = new ArrayList<>();
            for (ResponseStatDto responseStatDto : views) {
                if (responseStatDto.getApp().equals("/events/" + dto.getId())) eventViews.add(responseStatDto);
            }
            dto.setViews(eventViews.size());
        }
        return eventShortDtos;
    }

    private int setViews(LocalDateTime created, Long eventId) {
        List<ResponseStatDto> views = statClient.getViewStats(created, LocalDateTime.now(),
                List.of("/events/" + eventId), true);
        return views.size();
    }

    private EventFullDto toFullWithViewsAndComments(Event event) {
        EventFullDto fullDto = EventMapper.toFullDto(event);
        fullDto.setViews(setViews(event.getCreatedOn(), fullDto.getId()));
        fullDto.setComments(commentRepository.findByEventId(event.getId()).size());
        return fullDto;
    }
}
