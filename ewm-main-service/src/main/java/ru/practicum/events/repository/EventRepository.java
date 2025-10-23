package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllAndEventDateIsAfterAndEventDateIsBefore(LocalDateTime start, LocalDateTime end);

    Page<Event> findAllAndEventDateIsAfter(LocalDateTime start);

    Page<Event> findAllAndEventDateIsBefore(LocalDateTime end);

    List<Event> findByInitiatorId(Long userId);
}
