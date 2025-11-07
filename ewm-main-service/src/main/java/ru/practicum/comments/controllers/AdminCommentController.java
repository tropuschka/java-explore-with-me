package ru.practicum.comments.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentAdmin(commentId);
    }

    @GetMapping("/{commentId}")
    public CommentReturnDto getUserComment(@PathVariable Long commentId) {
        return commentService.getCommentByIdAdmin(commentId);
    }

    @GetMapping("/users/{userId}")
    public List<CommentReturnDto> getAllUserComments(@PathVariable Long userId) {
        return commentService.getUserCommentsAll(userId);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentReturnDto> getAllEventComments(@PathVariable Long eventId) {
        return commentService.getAllEventComments(eventId);
    }
}
