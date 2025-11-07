package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;
import ru.practicum.locations.model.Location;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateIsAfterAndEventDateIsBefore(LocalDateTime start, LocalDateTime end);

    List<Event> findByEventDateIsAfter(LocalDateTime start);

    List<Event> findByEventDateIsBefore(LocalDateTime end);

    Page<Event> findByInitiatorId(Long userId, PageRequest pageRequest);

    List<Event> findByCategoryId(Long catId);

    List<Event> findByLocationId(Long locId);
}
