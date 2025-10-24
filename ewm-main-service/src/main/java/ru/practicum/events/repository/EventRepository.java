package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateIsAfterAndEventDateIsBefore(LocalDateTime start, LocalDateTime end);

    List<Event> findByEventDateIsAfter(LocalDateTime start);

    List<Event> findByEventDateIsBefore(LocalDateTime end);

    List<Event> findByInitiatorId(Long userId);

    List<Event> findByCategoryId(Long catId);
}
