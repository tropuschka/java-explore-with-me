package ru.practicum.compilations.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.compilations.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationsController {
    private final CompilationService compilationService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @Autowired
    public AdminCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @Validated
    public ResponseEntity<CompilationDto> addCompilation(@RequestHeader(userIdHeader) Long adminId,
                                                         @Valid @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.addCompilation(adminId, newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@RequestHeader(userIdHeader) Long adminId, @PathVariable Long compId) {
        compilationService.deleteCompilation(adminId, compId);
    }

    @PatchMapping("/compId")
    public ResponseEntity<CompilationDto> updateCompilation(@RequestHeader(userIdHeader) Long adminId,
                                                            @PathVariable Long compId,
                                                            @RequestBody UpdateCompilationRequest updateCompilation) {
        return new ResponseEntity<>(compilationService.updateCompilation(adminId, compId, updateCompilation),
                HttpStatus.OK);
    }
}
