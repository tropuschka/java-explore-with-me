package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.*;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.model.Event;

import java.util.List;

public interface EventService {
    List<EventShortDto> search(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                               boolean onlyAvailable, String sort, int from, int size,
                               HttpServletRequest hHttpServletRequest);

    EventFullDto getEvent(Long eventId, HttpServletRequest httpServletRequest);

    List<EventFullDto> adminSearch(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                   String rangeEnd, int from, int size);

    EventFullDto adminEventUpdate(Long eventId, UpdateEventAdminRequest eventAdminRequest);

    List<EventShortDto> getUserEvents(Long userId);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto userEventUpdate(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);
}
