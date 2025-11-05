package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.mapping.RequestMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Request;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.repository.RequestRepository;
import ru.practicum.events.status.EventRequestStatus;
import ru.practicum.events.status.EventState;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        checkUser(userId);
        List<Request> requests = requestRepository.findByParticipantId(userId);
        return requests.stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public ParticipationRequestDto postRequest(Long userId, Long eventId, HttpServletRequest httpServletRequest) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        checkInitiator(userId, event);
        Optional<Request> check = checkRequest(userId, eventId);
        if (check.isPresent()) {
            throw new ConflictException("Запрос на мероприятие с ID " + eventId + " от пользователя с ID " + userId
                    + " уже создан");
        }
        Request request = new Request(null, eventId, userId, EventRequestStatus.PENDING, LocalDateTime.now());
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(EventRequestStatus.CONFIRMED);
        }
        Request saved = requestRepository.save(request);
        List<String> views = event.getViews();
        if (!views.contains(httpServletRequest.getRemoteAddr())) {
            views.add(httpServletRequest.getRemoteAddr());
        }
        eventRepository.save(event);
        return RequestMapper.toDto(saved);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        checkUser(userId);
        Request request = checkRequest(requestId);
        request.setStatus(EventRequestStatus.CANCELED);
        Request saved = requestRepository.save(request);
        return RequestMapper.toDto(saved);
    }

    private void checkUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь с ID " + userId + " не найден");
    }

    private Event checkEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие с ID " + eventId + " недоступно");
        }
        if (event.getParticipantAmount() + 1 > event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Мест на мероприятии с ID " + eventId + " больше нет");
        }
        return event;
    }

    private Optional<Request> checkRequest(Long userId, Long eventId) {
        return requestRepository.findByParticipantIdAndEventId(userId, eventId);
    }

    private Request checkRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + requestId + " не найден"));
    }

    private void checkInitiator(Long userId, Event event) {
        User initiator = event.getInitiator();
        if (initiator.getId().equals(userId)) {
            throw new ConflictException("Пользователь является инициатором события");
        }
    }
}
