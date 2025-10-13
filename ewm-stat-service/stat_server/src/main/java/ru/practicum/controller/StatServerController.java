package ru.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.service.StatServerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ResponseStatDto;
import ru.practicum.dto.StatDto;
import ru.practicum.validation.Validation;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
public class StatServerController {
    private final StatServerService statServerService;
    private static final String timeFormat = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public StatServerController(StatServerService statServerService) {
        this.statServerService = statServerService;
    }

    @PostMapping("/hit")
    @Validated({Validation.Create.class})
    public ResponseEntity<StatDto> addStat(@RequestBody @Valid StatDto statDto) {
        return new ResponseEntity<>(statServerService.add(statDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ResponseStatDto>> getStat(
            @RequestParam @DateTimeFormat(pattern = timeFormat) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = timeFormat) LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        return new ResponseEntity<>(statServerService.getStat(start, end, uris, unique), HttpStatus.OK);
    }
}
