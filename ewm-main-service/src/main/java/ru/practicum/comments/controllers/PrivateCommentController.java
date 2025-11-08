package ru.practicum.comments.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/{userId}/comments")
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{eventId}")
    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    public CommentReturnDto addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                       @Valid @RequestBody CommentDto comment) {
        return commentService.addComment(userId, eventId, comment);
    }

    @PatchMapping("/{commentId}")
    @Validated
    public CommentReturnDto updateComment(@PathVariable Long userId, @PathVariable Long commentId,
                                          @Valid @RequestBody CommentDto commentDto) {
        return commentService.updateComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentReturnDto getUserComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return commentService.getUserCommentById(userId, commentId);
    }

    @GetMapping
    public List<CommentReturnDto> getAllUserComments(@PathVariable Long userId) {
        return commentService.getUserCommentsAll(userId);
    }
}
