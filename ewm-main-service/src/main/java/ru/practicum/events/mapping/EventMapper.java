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
import java.util.ArrayList;
import java.util.HashSet;

import static ru.practicum.events.model.Event.timeFormat;

public class EventMapper {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);

    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getParticipantAmount(), event.getEventDate().format(formatter), event.getId(),
                UserMapper.toShortDto(event.getInitiator()), event.isPaid(), event.getTitle(), event.getViewsAmount());
    }

    public static EventFullDto toFullDto(Event event) {
        String publ;
        if (event.getPublished() == null) publ = null;
        else publ = event.getPublished().format(formatter);
        return new EventFullDto(event.getAnnotation(), CategoryMapper.toDto(event.getCategory()),
                event.getParticipantAmount(), event.getCreatedOn().format(formatter), event.getDescription(),
                event.getEventDate().format(formatter), event.getId(), UserMapper.toShortDto(event.getInitiator()),
                toLocationDto(event.getLocation()), event.isPaid(), event.getParticipantLimit(),
                publ, event.isRequestModeration(), event.getState().toString(),
                event.getTitle(), event.getViewsAmount());
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static Event toEvent(NewEventDto newEventDto, Location location) {
        if (newEventDto.getParticipantLimit() == null) newEventDto.setParticipantLimit(0);
        if (newEventDto.getRequestModeration() == null) newEventDto.setRequestModeration(true);
        return new Event(null, newEventDto.getAnnotation(), null,
                LocalDateTime.parse(newEventDto.getEventDate(), formatter), null, newEventDto.isPaid(),
                newEventDto.getTitle(), new ArrayList<>(), new HashSet<>(), null,
                newEventDto.getParticipantLimit(), newEventDto.getRequestModeration(), newEventDto.getDescription(),
                LocalDateTime.now(), location, EventState.PENDING);
    }

    private static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
