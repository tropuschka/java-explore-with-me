package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;

public interface CommentService {
    CommentReturnDto addComment(Long userId, Long eventId, CommentDto commentDto);

    CommentReturnDto updateComment(Long userId, Long commentId, CommentDto commentDto);
}
