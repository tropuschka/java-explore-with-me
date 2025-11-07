package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.mapping.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.comments.status.CommentStatus;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;

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
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setUser(user);
        comment.setEvent(event);
        comment.setStatus(CommentStatus.PUBLISHED);
        comment.setPublished(LocalDateTime.now());
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toReturnDto(saved);
    }

    @Override
    public CommentReturnDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment);
        comment.setText(commentDto.getText());
        Comment saved = commentRepository.save(comment);
        return CommentMapper.toReturnDto(saved);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с ID " + eventId + " не найдено"));
    }

    private Comment checkComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с ID " + commentId + " не найден"));
        if (comment.getStatus().equals(CommentStatus.DELETED)) {
            throw new ConflictException("Нельзя редактировать удаленные комментарии");
        }
        return comment;
    }

    private void checkAuthor(Long userId, Comment comment) {
        if (!userId.equals(comment.getUser().getId())) {
            throw new ForbiddenException("Изменять комментарий может только его автор");
        }
    }
}
