package ru.practicum.events.mapping;

import ru.practicum.categories.mapping.CategoryMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.events.status.EventState;
import ru.practicum.users.mapping.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class EventMapper {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getParticipantAmount(), event.getEventDate().toString(), event.getId(),
                UserMapper.toShortDto(event.getInitiator()), event.isPaid(), event.getTitle(), event.getViews());
    }

    public static EventFullDto toFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getParticipantAmount(), event.getCreatedOn().toString(), event.getDescription(),
                event.getEventDate().toString(), event.getId(), UserMapper.toShortDto(event.getInitiator()),
                toLocationDto(event.getLocation()), event.isPaid(), event.getParticipantLimit(),
                event.getPublished().toString(), event.isRequestModeration(), event.getState().toString(),
                event.getTitle(), event.getViews());
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return new Event(null, newEventDto.getAnnotation(), null,
                LocalDateTime.parse(newEventDto.getEventDate(), formatter), null, newEventDto.isPaid(),
                newEventDto.getTitle(), 0L, new HashSet<>(), null, newEventDto.getParticipantLimit(),
                newEventDto.isRequestModeration(), newEventDto.getDescription(), LocalDateTime.now(),
                toLocation(newEventDto.getLocation()), EventState.PENDING);
    }

    private static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
