package ru.practicum.locations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.locations.model.Location;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationReturnDto;
import ru.practicum.locations.mapping.LocationMapper;
import ru.practicum.locations.repository.LocationRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public LocationReturnDto addLocation(LocationDto locationDto) {
        checkLocationDuplication(locationDto);
        Location location = LocationMapper.toLocation(locationDto);
        Location saved = locationRepository.save(location);
        return LocationMapper.toReturnDto(saved);
    }

    private void checkLocationDuplication(LocationDto locationDto) {
        Optional<Location> loc = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
        if (loc.isPresent()) throw new ConflictException("Локация с координатами (" + locationDto.getLat() + ", "
                + locationDto.getLon() + ") уже добавлена");
    }
}
