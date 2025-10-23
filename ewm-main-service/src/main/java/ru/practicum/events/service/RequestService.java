package ru.practicum.events.service;

import ru.practicum.events.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(Long headerId, Long userId);

    ParticipationRequestDto postRequest(Long headerId, Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long headerId, Long userId, Long requestId);
}
