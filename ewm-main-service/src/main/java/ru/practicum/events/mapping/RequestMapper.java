package ru.practicum.events.mapping;

import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.model.Request;

public class RequestMapper {
    public static ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(request.getCreated().toString(), request.getEventId(), request.getId(),
                request.getParticipantId(), request.getStatus().toString());
    }
}
