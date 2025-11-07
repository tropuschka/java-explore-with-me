package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;

import java.util.List;

public interface CommentService {
    CommentReturnDto addComment(Long userId, Long eventId, CommentDto commentDto);

    CommentReturnDto updateComment(Long userId, Long commentId, CommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

    CommentReturnDto getUserCommentById(Long userId, Long commentId);

    List<CommentReturnDto> getUserCommentsAll(Long userId);

    void deleteCommentAdmin(Long commentId);

    CommentReturnDto getCommentByIdAdmin(Long commentId);
}
