package main.java.ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.java.ru.practicum.service.StatServerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.StatDto;
import ru.practicum.validation.Validation;

@RestController
@RequiredArgsConstructor
@Validated
public class StatServerController {
    StatServerService statServerService;

    @PostMapping("/hit")
    @Validated({Validation.Create.class})
    public ResponseEntity<StatDto> addStat(@RequestBody @Valid StatDto statDto) {
        return new ResponseEntity<>(statServerService.add(statDto), HttpStatus.CREATED);
    }
}
