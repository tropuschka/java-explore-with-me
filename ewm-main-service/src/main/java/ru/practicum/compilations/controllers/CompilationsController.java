package ru.practicum.compilations.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
public class CompilationsController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
