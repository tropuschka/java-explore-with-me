package ru.practicum.comments.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentReturnDto> getEventComments(@PathVariable Long eventId) {
        return commentService.getAllEventComments(eventId);
    }
}
