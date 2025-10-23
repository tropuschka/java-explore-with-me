package ru.practicum.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByIdIn(List<Long> ids, PageRequest page);
}
