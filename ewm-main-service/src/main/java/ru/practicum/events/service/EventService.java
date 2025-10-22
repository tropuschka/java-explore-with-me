package ru.practicum.events.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.status.EventState;

import java.util.List;

public interface EventService {
    List<EventShortDto> search(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                               boolean onlyAvailable, String sort, int from, int size,
                               HttpServletRequest hHttpServletRequest);

    EventFullDto getEvent(Long eventId, HttpServletRequest httpServletRequest);

    List<EventFullDto> adminSearch(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                   String rangeEnd, int from, int size);
}
