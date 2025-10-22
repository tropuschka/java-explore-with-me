package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.mapping.RequestMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Request;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.repository.RequestRepository;
import ru.practicum.events.status.EventRequestStatus;
import ru.practicum.exceptions.ConditionsNotMetException;
import ru.practicum.exceptions.DuplicationException;
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
    public List<ParticipationRequestDto> getUserRequests (Long userId) {
        checkUser(userId);
        List<Request> requests = requestRepository.findByParticipantId(userId);
        return requests.stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public ParticipationRequestDto postRequest(Long userId, Long eventId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        checkInitiator(userId, event);
        Optional<Request> check = checkRequest(userId, eventId);
        if (check.isPresent()) throw new DuplicationException("Запрос уже создан");
        Request request = new Request(null, eventId, userId, EventRequestStatus.PENDING, LocalDateTime.now());
        if (!event.isRequestModeration()) request.setStatus(EventRequestStatus.CONFIRMED);
        Request saved = requestRepository.save(request);
        return RequestMapper.toDto(saved);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        checkUser(userId);
        Request request = checkRequest(requestId);
        request.setStatus(EventRequestStatus.CANCELLED);
        Request saved = requestRepository.save(request);
        return RequestMapper.toDto(saved);
    }

    private void checkUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
    }

    private Event checkEvent(Long eventId) {
        Optional<Event> optEvent = eventRepository.findById(eventId);
        if (optEvent.isEmpty()) throw new NotFoundException("Событие не найдено");
        Event event = optEvent.get();
        if (event.getEventDate() == null) {
            throw new ConditionsNotMetException("Событие недоступно");
        }
        if (event.getParticipantAmount() + 1 > event.getParticipantLimit()) {
            throw new ConditionsNotMetException("Мест на мероприятии больше нет");
        }
        return event;
    }

    private Optional<Request> checkRequest(Long userId, Long eventId) {
        return Optional.of(requestRepository.findByParticipantIdAndEventId(userId, eventId));
    }

    private Request checkRequest(Long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) throw new NotFoundException("Запрос не найден");
        return request.get();
    }

    private void checkInitiator(Long userId, Event event) {
        User initiator = event.getInitiator();
        if (initiator.getId().equals(userId)) {
            throw new ConditionsNotMetException("Пользователь является инициатором события");
        }
    }
}
