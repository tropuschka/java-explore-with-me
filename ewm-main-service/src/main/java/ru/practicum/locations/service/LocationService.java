package ru.practicum.locations.service;

import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationReturnDto;

public interface LocationService {
    LocationReturnDto addLocation(LocationDto locationDto);

    LocationReturnDto updateLocation(Long locId, LocationDto locationDto);
}
