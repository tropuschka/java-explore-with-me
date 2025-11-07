package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}
