package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapping.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.status.EventSort;
import ru.practicum.events.status.EventState;
import ru.practicum.exceptions.ConditionsNotMetException;
import ru.practicum.client.StatClient;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Override
    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, String rangeStart,
                                      String rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                      HttpServletRequest httpServletRequest) {
        List<Event> events = search(rangeStart, rangeEnd);

        // Вероятно, все эти проверки можно было засунуть в репозиторий,
        // но, думаю, вышло бы больше методов, чем если использовать много стримов ㅠㅠ
        if (text != null && !text.isBlank()) {
            text = text.toLowerCase();
            String finalText = text;
            events = events.stream().filter(e -> e.getAnnotation().toLowerCase().contains(finalText)
                || e.getTitle().toLowerCase().contains(finalText)
                || e.getDescription().toLowerCase().contains(finalText))
                    .toList();
        }
        if (categories != null && !categories.isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getCategory() != null)
                    .filter(e -> categories.contains(e.getCategory().getId()))
                    .toList();
        }
        if (paid != null) {
            events = events.stream()
                    .filter(e -> e.isPaid() == paid)
                    .toList();
        }
        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantAmount() < e.getParticipantLimit())
                    .toList();
        }

        EventSort sortEnum;
        try {
            sortEnum = EventSort.valueOf(sort.toUpperCase());
        } catch (Exception e) {
            throw new ConditionsNotMetException("Некорректная сортировка");
        }
        if (sortEnum.equals(EventSort.EVENT_DATE)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .toList();
        } else if (sortEnum.equals(EventSort.VIEWS)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getViews))
                    .toList();
        }

        List<EventShortDto> searchList = new ArrayList<>();
        for (int i = from; i < from + size; i++) {
            searchList.add(EventMapper.toShortDto(events.get(i)));
        }
        statClient.saveStat(httpServletRequest, "events/search");
        return searchList;
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = searchEvent(eventId);
        if (event.getPublished() == null) throw new ConditionsNotMetException("Событие недоступно");
        statClient.saveStat(httpServletRequest, "events/get");
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> adminSearch(List<Long> users, List<String> states, List<Long> categories,
                                          String rangeStart, String rangeEnd, int from, int size) {
        List<Event> events = search(rangeStart, rangeEnd);

        if (users != null && !users.isEmpty()) {
            events = events.stream()
                    .filter(e -> users.contains(e.getInitiator().getId()))
                    .toList();
        }
        if (states != null && !states.isEmpty()) {
            List<EventState> statesEnum = states.stream().map(String::toUpperCase).map(EventState::valueOf).toList();
            events = events.stream()
                    .filter(e -> statesEnum.contains(e.getState()))
                    .toList();
        }
        if (categories != null && !categories.isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getCategory() != null)
                    .filter(e -> categories.contains(e.getCategory().getId()))
                    .toList();
        }

        List<EventFullDto> searchList = new ArrayList<>();
        for (int i = from; i < from + size; i++) {
            searchList.add(EventMapper.toFullDto(events.get(i)));
        }
        return searchList;
    }

    private Event searchEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) throw new NotFoundException("Событие не найдено");
        return event.get();
    }

    private List<Event> search(String rangeStart, String rangeEnd) {
        LocalDateTime searchStart = null;
        LocalDateTime searchEnd = null;
        if (rangeStart == null && rangeEnd == null) {
            searchStart = LocalDateTime.now();
        }
        if (rangeStart != null) searchStart = LocalDateTime.parse(rangeStart, formatter);
        if (rangeEnd != null) searchEnd = LocalDateTime.parse(rangeEnd, formatter);


        Page<Event> searchEvent;
        if (searchStart != null && searchEnd != null) {
            searchEvent = eventRepository.findAllAndEventDateIsAfterAndEventDateIsBefore(searchStart, searchEnd);
        } else if (searchStart != null) {
            searchEvent = eventRepository.findAllAndEventDateIsAfter(searchStart);
        } else {
            searchEvent = eventRepository.findAllAndEventDateIsBefore(searchEnd);
        }
        return searchEvent.stream().toList();
    }
}
