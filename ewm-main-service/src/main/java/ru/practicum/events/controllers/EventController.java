package ru.practicum.events.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.SearchDto;
import ru.practicum.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> search(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                      @RequestParam(defaultValue = "ID") String sort,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      HttpServletRequest httpServletRequest) {
        return eventService.search(new SearchDto(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size), httpServletRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest httpServletRequest) {
        return eventService.getEvent(eventId, httpServletRequest);
    }
}
