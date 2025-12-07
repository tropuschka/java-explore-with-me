package ru.practicum.comments.mapping;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentReturnDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.events.mapping.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.users.mapping.UserMapper;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

import static ru.practicum.events.mapping.EventMapper.formatter;

public class CommentMapper {
    public static CommentReturnDto toReturnDto(Comment comment) {
        return new CommentReturnDto(comment.getId(), UserMapper.toShortDto(comment.getUser()),
                EventMapper.toCommentDto(comment.getEvent()), comment.getText(),
                comment.getPublished().format(formatter));
    }

    public static Comment toComment(CommentDto commentDto, User user, Event event) {
        return new Comment(null, user, event, commentDto.getText(), LocalDateTime.now());
    }
}
