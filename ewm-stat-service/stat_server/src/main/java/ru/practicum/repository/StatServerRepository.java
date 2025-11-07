package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ResponseStatDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerRepository extends JpaRepository<Stat, Long> {
    @Query("select new ru.practicum.dto.ResponseStatDto(stat.app, stat.uri, count(distinct stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between :start and :end " +
            "and (:uris is null or stat.uri in :uris) " +
            "group by stat.app, stat.uri " +
            "order by count(distinct stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWhereIpIsUnique(LocalDateTime start,
                                                                              LocalDateTime end,
                                                                              List<String> uris);

    @Query("select new ru.practicum.dto.ResponseStatDto(stat.app, stat.uri, count(stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between :start and :end " +
            "and stat.uri in :uris " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWithUris(LocalDateTime start,
                                                                       LocalDateTime end,
                                                                       List<String> uris);

    @Query("select new ru.practicum.dto.ResponseStatDto(stat.app, stat.uri, count(stat.ip)) " +
            "from Stat as stat " +
            "where stat.timestamp between :start and :end " +
            "group by stat.app, stat.uri " +
            "order by count(stat.ip) desc")
    List<ResponseStatDto> findAllByTimestampBetweenStartAndEndWithoutUris(LocalDateTime start,
                                                                          LocalDateTime end);
}
