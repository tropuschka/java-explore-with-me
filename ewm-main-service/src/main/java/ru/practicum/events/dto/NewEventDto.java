package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewEventDto {
    private String annotation;
    private Long category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    private String title;
}
