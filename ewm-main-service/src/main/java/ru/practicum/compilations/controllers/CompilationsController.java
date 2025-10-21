package ru.practicum.compilations.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationDto;

@RestController
@RequestMapping(path = "/compilations")
public class CompilationsController {
    @GetMapping
    public CompilationDto getCompilations(@RequestParam boolean pinned, @RequestParam int from, @RequestParam int size) {
        return new CompilationDto();
    }
}
