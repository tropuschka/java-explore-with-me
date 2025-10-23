package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
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
}
