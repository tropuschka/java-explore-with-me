package ru.practicum.events.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.categories.model.Category;
import ru.practicum.events.status.EventRequestStatus;
import ru.practicum.events.status.EventState;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "event")
public class Event {
    public static final String timeFormat = "yyyy-MM-dd HH:mm:ss";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;
    @Column(name = "paid", nullable = false)
    private boolean paid;
    @Column(name = "title", nullable = false)
    private String title;
    private int views;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "participation", joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private Set<Request> requests;
    @Column(name = "published")
    private LocalDateTime published;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;
    @Column(name = "request_moderation", nullable = false)
    private boolean requestModeration;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    private Location location;
    @Column(name = "state", nullable = false)
    private EventState state;

    public int getParticipantAmount() {
        Set<Request> approved = requests.stream()
                .filter(r -> r.getStatus().equals(EventRequestStatus.CONFIRMED)).collect(Collectors.toSet());
        return approved.size();
    }
}
