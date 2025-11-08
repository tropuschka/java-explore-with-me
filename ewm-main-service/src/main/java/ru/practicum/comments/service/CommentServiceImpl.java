package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.mapping.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.status.EventState;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentReturnDto addComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = checkUser(userId);
        Event event = checkEvent(eventId);
        Comment comment = CommentMapper.toComment(commentDto, user, event);
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toReturnDto(saved);
    }

    @Override
    public CommentReturnDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment, "Изменять комментарий может только его автор");
        comment.setText(commentDto.getText());
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toReturnDto(saved);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment, "Удалить комментарий может только его автор или администратор");
        commentRepository.delete(comment);
    }

    @Override
    public CommentReturnDto getUserCommentById(Long userId, Long commentId) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment, "Просматривать отдельный комментарий может только его автор или администратор");
        return CommentMapper.toReturnDto(comment);
    }

    @Override
    public List<CommentReturnDto> getUserCommentsAll(Long userId) {
        checkUser(userId);
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream().map(CommentMapper::toReturnDto).toList();
    }

    @Override
    public void deleteCommentAdmin(Long commentId) {
        Comment comment = checkComment(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public CommentReturnDto getCommentByIdAdmin(Long commentId) {
        Comment comment = checkComment(commentId);
        return CommentMapper.toReturnDto(comment);
    }

    @Override
    public List<CommentReturnDto> getAllEventComments(Long eventId) {
        checkEvent(eventId);
        List<Comment> comments = commentRepository.findByEventId(eventId);
        return comments.stream().map(CommentMapper::toReturnDto).toList();
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    private Event checkEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Комментарии можно оставлять только к опубликованным событиям");
        }
        return event;
    }

    private Comment checkComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));
    }

    private void checkAuthor(Long userId, Comment comment, String text) {
        if (!userId.equals(comment.getUser().getId())) {
            throw new ForbiddenException(text);
        }
    }
}
