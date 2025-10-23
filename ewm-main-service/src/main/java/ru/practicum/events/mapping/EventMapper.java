package ru.practicum.events.mapping;

import ru.practicum.categories.mapping.CategoryMapper;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.LocationDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.users.mapping.UserMapper;

public class EventMapper {
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

    private static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
