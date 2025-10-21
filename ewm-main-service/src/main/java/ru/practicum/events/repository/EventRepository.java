package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT new ru.practicum.events.dto.EventShortDto() " +
            "FROM Event event " +
            "WHERE event.id IN :ids " +
            "GROUP BY event.id " +
            "ORDER BY event.id")
    List<EventShortDto> findAllShortById(List<Long> ids);
}
