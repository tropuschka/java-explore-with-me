package ru.practicum.compilations.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.events.model.Event;

import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "compilation")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "pinned", nullable = false)
    private boolean pinned;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "compilation_event", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
}
