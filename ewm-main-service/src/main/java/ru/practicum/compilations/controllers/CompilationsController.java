package ru.practicum.compilations.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam int from, @RequestParam int size) {
        return new ResponseEntity<>(compilationService.getCompillations(pinned, from, size), HttpStatus.OK);
    }
}
