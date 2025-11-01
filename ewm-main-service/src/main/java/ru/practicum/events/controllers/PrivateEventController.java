package ru.practicum.events.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<EventShortDto>> getUserEvents(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(eventService.getUserEvents(userId, from, size), HttpStatus.OK);
    }

    @PostMapping
    @Validated
    public ResponseEntity<EventFullDto> postEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEvent) {
        return new ResponseEntity<>(eventService.createEvent(userId, newEvent), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable(name = "userId") Long userId,
                                                     @PathVariable(name = "eventId") Long eventId,
                                                     HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(eventService.getUserEventById(userId, eventId, httpServletRequest), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    @Validated
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable(name = "userId") Long userId,
                                                    @PathVariable(name = "eventId") Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return new ResponseEntity<>(eventService.userEventUpdate(userId, eventId, updateEventUserRequest),
                HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@PathVariable(name = "userId") Long userId,
                                                                          @PathVariable(name = "eventId") Long eventId) {
        return new ResponseEntity<>(eventService.getEventRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    @Validated
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@PathVariable(name = "userId") Long userId,
                                                                              @PathVariable(name = "eventId") Long eventId,
                                                                              @Valid @RequestBody EventRequestStatusUpdateRequest request) {
        return new ResponseEntity<>(eventService.updateRequestStatus(userId, eventId, request), HttpStatus.OK);
    }
}
