package ru.practicum.events.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.dto.participation.EventRequestStatusUpdateRequest;
import ru.practicum.events.dto.participation.EventRequestStatusUpdateResult;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @Autowired
    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public EventFullDto postEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEvent) {
        return eventService.createEvent(userId, newEvent);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(name = "userId") Long userId,
                                                     @PathVariable(name = "eventId") Long eventId,
                                                     HttpServletRequest httpServletRequest) {
        return eventService.getUserEventById(userId, eventId, httpServletRequest);
    }

    @PatchMapping("/{eventId}")
    @Validated
    public EventFullDto updateEvent(@PathVariable(name = "userId") Long userId,
                                                    @PathVariable(name = "eventId") Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.userEventUpdate(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable(name = "userId") Long userId,
                                                                          @PathVariable(name = "eventId") Long eventId) {
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @Validated
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable(name = "userId") Long userId,
                                                                              @PathVariable(name = "eventId") Long eventId,
                                                                              @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.updateRequestStatus(userId, eventId, request);
    }
}
