package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.mapping.StatMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatServerRepository;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ResponseStatDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServerServiceImpl implements StatServerService {
    private final StatServerRepository statServerRepository;

    @Override
    public StatDto add(StatDto statDto) {
        Stat stat = StatMapper.toStat(statDto);
        return StatMapper.toStatDto(statServerRepository.save(stat));
    }

    @Override
    public List<ResponseStatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new ClassCastException("Начало периода должно быть до его конца");
        }
        if (unique) {
            return statServerRepository.findAllByTimestampBetweenStartAndEndWhereIpIsUnique(start, end, uris);
        } else if (uris.isEmpty()) {
            return statServerRepository.findAllByTimestampBetweenStartAndEndWithoutUris(start, end);
        } else {
            return statServerRepository.findAllByTimestampBetweenStartAndEndWithUris(start, end, uris);
        }
    }
}
