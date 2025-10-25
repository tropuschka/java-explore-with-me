package ru.practicum.events.mapping;

import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.model.Request;

import java.time.format.DateTimeFormatter;

import static ru.practicum.events.model.Event.timeFormat;

public class RequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);

    public static ParticipationRequestDto toDto(Request request) {
        return new ParticipationRequestDto(request.getCreated().format(formatter), request.getEventId(), request.getId(),
                request.getParticipantId(), request.getStatus().toString());
    }
}
