package ru.practicum.events.mapping;

import ru.practicum.categories.mapping.CategoryMapper;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.users.mapping.UserMapper;

public class EventMapper {
    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getParticipantAmount(), event.getEventDate().toString(), event.getId(),
                UserMapper.toShortDto(event.getInitiator()), event.isPaid(), event.getTitle(), event.getViews());
    }
}
