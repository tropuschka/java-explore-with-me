package ru.practicum.events.controllers;

import jakarta.persistence.Access;
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

import static ru.practicum.users.controllers.AdminUserController.userIdHeader;

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
    public ResponseEntity<List<EventShortDto>> getUserEvents(@RequestHeader(userIdHeader) Long headerId,
                                                             @PathVariable Long userId) {
        return new ResponseEntity<>(eventService.getUserEvents(headerId, userId), HttpStatus.OK);
    }

    @PostMapping
    @Validated
    public ResponseEntity<EventFullDto> postEvent(@RequestHeader(userIdHeader) Long headerId,
                                                  @PathVariable Long userId, @Valid @RequestBody NewEventDto newEvent) {
        return new ResponseEntity<>(eventService.createEvent(headerId, userId, newEvent), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@RequestHeader(userIdHeader) Long headerId,
                                                     @PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getUserEventById(headerId, userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    @Validated
    public ResponseEntity<EventFullDto> updateEvent(@RequestHeader(userIdHeader) Long headerId,
                                                    @PathVariable Long userId, @PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return new ResponseEntity<>(eventService.userEventUpdate(headerId, userId, eventId, updateEventUserRequest),
                HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequests(@RequestHeader(userIdHeader) Long headerId,
                                                                          @PathVariable Long userId,
                                                                          @PathVariable Long eventId) {
        return new ResponseEntity<>(eventService.getEventRequests(headerId, userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    @Validated
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestStatus(@RequestHeader(userIdHeader) Long headerId,
                                                                              @PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        return new ResponseEntity<>(eventService.updateRequestStatus(headerId, userId, eventId, request), HttpStatus.OK);
    }
}
