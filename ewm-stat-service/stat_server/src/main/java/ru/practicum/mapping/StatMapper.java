package ru.practicum.mapping;

import ru.practicum.model.Stat;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static Stat toStat(StatDto statDto) {
        Stat stat = new Stat();
        stat.setId(statDto.getId());
        stat.setIp(statDto.getIp());
        stat.setApp(statDto.getApp());
        stat.setUri(statDto.getUri());
        stat.setTimestamp(LocalDateTime.parse(statDto.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return stat;
    }

    public static StatDto toStatDto(Stat stat) {
        return new StatDto(stat.getId(), stat.getApp(), stat.getUri(), stat.getIp(), stat.getTimestamp().toString());
    }
}
