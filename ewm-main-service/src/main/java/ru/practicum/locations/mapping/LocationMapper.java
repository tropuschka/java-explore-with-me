package ru.practicum.locations.mapping;

import ru.practicum.locations.model.Location;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationReturnDto;

public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        return new Location(null, locationDto.getLat(), locationDto.getLon());
    }

    public static LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }

    public static LocationReturnDto toReturnDto(Location location) {
        return new LocationReturnDto(location.getId(), location.getLat(), location.getLon());
    }
}
