package main.java.ru.practicum.repository;

import main.java.ru.practicum.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatServerRepository extends JpaRepository<Stat, Long> {
}
