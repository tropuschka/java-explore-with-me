package ru.practicum.compilations.mapping;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapping.EventMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(null, newCompilationDto.getTitle(), newCompilationDto.isPinned(),
                new HashSet<>());
    }

    public static CompilationDto toDto(Compilation compilation) {
        List<EventShortDto> compilationEvents;
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            compilationEvents = compilation.getEvents().stream()
                    .map(EventMapper::toShortDto).collect(Collectors.toList());
        } else compilationEvents = new ArrayList<>();
        return new CompilationDto(compilationEvents, compilation.getId(), compilation.isPinned(),
                compilation.getTitle());
    }
}
