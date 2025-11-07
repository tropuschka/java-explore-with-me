package ru.practicum.events.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.SearchDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> search(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        return eventService.adminSearch(new SearchDto(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    @Validated
    public EventFullDto update(@PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.adminEventUpdate(eventId, updateEventAdminRequest);
    }
}
