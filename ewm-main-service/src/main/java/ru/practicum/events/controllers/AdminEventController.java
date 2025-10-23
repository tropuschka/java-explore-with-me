package ru.practicum.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @Autowired
    public AdminEventController (EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> search(@RequestHeader(userIdHeader) Long adminId,
                                                     @RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(
                eventService.adminSearch(adminId, users, states, categories, rangeStart, rangeEnd, from, size),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> update(@RequestHeader(userIdHeader) Long adminId, @PathVariable Long eventId,
                                               @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return new ResponseEntity<>(eventService.adminEventUpdate(adminId, eventId, updateEventAdminRequest),
                HttpStatus.OK);
    }
}
