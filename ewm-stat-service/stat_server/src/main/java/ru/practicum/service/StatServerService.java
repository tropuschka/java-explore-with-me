package ru.practicum.service;

import ru.practicum.dto.ResponseStatDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerService {
    StatDto add(StatDto statDto);

    List<ResponseStatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
