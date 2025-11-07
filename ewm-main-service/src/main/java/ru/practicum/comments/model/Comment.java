package ru.practicum.comments.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.comments.status.CommentStatus;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private User user;
    @Column(name = "event_id")
    private Event event;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    private CommentStatus status;
    @Column(name = "published")
    private LocalDateTime published;
}
