package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByParticipantId(Long userId);

    Optional<Request> findByParticipantIdAndEventId(Long userId, Long eventId);

    List<Request> findByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> ids);
}
