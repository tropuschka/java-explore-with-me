package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto postRequest(Long userId, Long eventId, HttpServletRequest httpServletRequest);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
