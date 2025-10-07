package main.java.ru.practicum.mapping;

import main.java.ru.practicum.model.Stat;
import ru.practicum.dto.StatDto;

public class StatMapper {
    public static Stat toStat(StatDto statDto) {
        Stat stat = new Stat();
        stat.setId(statDto.getId());
        stat.setIp(statDto.getIp());
        stat.setApp(statDto.getApp());
        stat.setUri(statDto.getUri());
        stat.setTimestamp(statDto.getTimestamp());
        return stat;
    }

    public static StatDto toStatDto(Stat stat) {
        return new StatDto(stat.getId(), stat.getApp(), stat.getUri(), stat.getIp(), stat.getTimestamp());
    }
}
