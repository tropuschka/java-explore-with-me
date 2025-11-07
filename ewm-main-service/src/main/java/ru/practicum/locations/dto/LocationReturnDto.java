package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LocationReturnDto {
    private Long id;
    private Float lat;
    private Float lon;
}
