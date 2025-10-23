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
    public ResponseEntity<List<EventShortDto>> getUserEvents(@PathVariable Long userId) {
        return new ResponseEntity<>(eventService.getUserEvents(userId), HttpStatus.OK);
    }

    @PostMapping
    @Validated
    public ResponseEntity<EventFullDto> postEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEvent) {
        return new ResponseEntity<>(eventService.createEvent(userId, newEvent), HttpStatus.CREATED);
    }
}
