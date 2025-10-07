package main.java.ru.practicum.service;

import lombok.RequiredArgsConstructor;
import main.java.ru.practicum.mapping.StatMapper;
import main.java.ru.practicum.model.Stat;
import main.java.ru.practicum.repository.StatServerRepository;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatDto;

@Service
@RequiredArgsConstructor
public class StatServerServiceImpl implements StatServerService {
    private final StatServerRepository statServerRepository;

    @Override
    public StatDto add(StatDto statDto) {
        Stat stat = StatMapper.toStat(statDto);
        return StatMapper.toStatDto(statServerRepository.save(stat));
    }
}
