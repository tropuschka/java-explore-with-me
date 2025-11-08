package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.dto.EventCommentDto;
import ru.practicum.users.dto.UserShortDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentReturnDto {
    private Long id;
    private UserShortDto user;
    private EventCommentDto event;
    private String text;
    private String published;
}
