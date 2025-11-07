package ru.practicum.locations.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    @Validated
    public LocationReturnDto addLocation(LocationDto locationDto) {
        return locationService.addLocation(locationDto);
    }
}
