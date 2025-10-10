package ru.practicum.repository;

import ru.practicum.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ResponseStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerRepository extends JpaRepository<Stat, Long> {
    @Query("select new ru.practicum.dto.StatResponseDto(stat.api, stat.uri, count(distinct stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between ?1 and ?2 " +
            "group by stat.api, stat.uri " +
            "order by count(distinct stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWhereIpIsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatResponseDto(stat.api, stat.uri, count(stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between ?1 and ?2 " +
            "group by stat.api, stat.uri " +
            "order by count(stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEnd(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatResponseDto(stat.api, stat.uri, count(distinct stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between ?1 and ?2 " +
            "and stat.uri in ?3 " +
            "group by stat.api, stat.uri " +
            "order by count(distinct stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWhereIpIsUniqueAndUriIn(LocalDateTime start,
                                                                                      LocalDateTime end,
                                                                                      List<String> uris);

    @Query("select new ru.practicum.dto.StatResponseDto(stat.api, stat.uri, count(stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between ?1 and ?2 " +
            "and stat.uri in ?3 " +
            "group by stat.api, stat.uri " +
            "order by count(stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWhereUriIn(LocalDateTime start,
                                                                                      LocalDateTime end,
                                                                                      List<String> uris);
}
