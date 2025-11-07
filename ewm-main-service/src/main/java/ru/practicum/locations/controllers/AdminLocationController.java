package ru.practicum.locations.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationReturnDto;
import ru.practicum.locations.service.LocationService;

@RestController
@RequestMapping("/location")
@Validated
public class AdminLocationController {
    private final LocationService locationService;

    @Autowired
    public AdminLocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationReturnDto addLocation(@RequestBody LocationDto locationDto) {
        return locationService.addLocation(locationDto);
    }

    @PatchMapping("/{locId}")
    public LocationReturnDto updateLocation(@PathVariable Long locId, @RequestBody LocationDto locationDto) {
        return locationService.updateLocation(locId, locationDto);
    }

    @DeleteMapping("/{locId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long locId) {
        locationService.deleteLocation(locId);
    }
}
