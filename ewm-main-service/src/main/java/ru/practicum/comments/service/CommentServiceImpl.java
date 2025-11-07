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
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

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
        comment.setStatus(CommentStatus.PENDING);
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
}
