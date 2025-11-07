package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private int views;
}
