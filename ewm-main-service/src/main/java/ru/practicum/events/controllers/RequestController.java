package ru.practicum.events.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.participation.ParticipationRequestDto;
import ru.practicum.events.service.RequestService;

import java.util.List;

import static ru.practicum.users.controllers.AdminUserController.userIdHeader;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Validated
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController (RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@RequestHeader(userIdHeader) Long headerId,
                                                                     @PathVariable Long userId) {
        return new ResponseEntity<>(requestService.getUserRequests(headerId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> postRequest(@RequestHeader(userIdHeader) Long headerId,
                                                               @RequestParam Long userId, @RequestParam Long eventId) {
        return new ResponseEntity<>(requestService.postRequest(headerId, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@RequestHeader(userIdHeader) Long headerId,
                                                                 @PathVariable Long userId,
                                                                 @PathVariable Long requestId) {
        return new ResponseEntity<>(requestService.cancelRequest(headerId, userId, requestId), HttpStatus.OK);
    }
}
