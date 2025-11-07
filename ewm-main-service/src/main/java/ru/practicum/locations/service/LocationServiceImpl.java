package ru.practicum.locations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.locations.model.Location;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationReturnDto;
import ru.practicum.locations.mapping.LocationMapper;
import ru.practicum.locations.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    @Override
    public LocationReturnDto addLocation(LocationDto locationDto) {
        checkLocationDuplication(locationDto.getLat(), locationDto.getLon(), null);
        Location location = LocationMapper.toLocation(locationDto);
        Location saved = locationRepository.save(location);
        return LocationMapper.toReturnDto(saved);
    }

    @Override
    public LocationReturnDto updateLocation(Long locId, LocationDto locationDto) {
        Location location = checkLocation(locId);
        if (locationDto.getLon() != null) location.setLon(locationDto.getLon());
        if (locationDto.getLat() != null) location.setLat(locationDto.getLat());
        checkLocationDuplication(location.getLat(), location.getLon(), locId);
        Location saved = locationRepository.save(location);
        return LocationMapper.toReturnDto(saved);
    }

    @Override
    public void deleteLocation(Long locId) {
        Location location = checkLocation(locId);
        List<Event> locEvents = eventRepository.findByLocationId(locId);
        if (!locEvents.isEmpty()) {
            throw new ConflictException("Нельзя удалить локацию, в которой есть события");
        }
        locationRepository.delete(location);
    }

    private void checkLocationDuplication(Float lat, Float lon, Long locId) {
        Optional<Location> loc = locationRepository.findByLatAndLon(lat, lon);
        if (loc.isPresent() && !(locId == null || locId.equals(loc.get().getId()))) {
            throw new ConflictException("Локация с координатами (" + lat + ", "
                    + lon + ") уже добавлена");
        }
    }

    private Location checkLocation(Long locId) {
        return locationRepository.findById(locId).orElseThrow(() -> new NotFoundException("Локация с ID " + locId +
                " не найдена"));
    }
}
