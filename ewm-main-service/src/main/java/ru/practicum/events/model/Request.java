package ru.practicum.events.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.status.EventRequestStatus;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "participation")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @Column(name = "participant_id", nullable = false)
    private Long participantId;
    @Column(name = "status", nullable = false)
    private EventRequestStatus status;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
