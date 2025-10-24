package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapping.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> search(Long adminId, List<Long> ids, int from, int size) {
        checkAdmin(adminId);
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(PageRequest.of(from, size, Sort.by(ASC, "id")));
        }
        else users = userRepository.findByIdIn(ids, PageRequest.of(from, size, Sort.by(ASC, "id")));
        return users.stream().map(UserMapper::toDto).toList();
    }

    @Override
    public UserDto addUser(Long adminId, NewUserRequest newUser) {
        checkAdmin(adminId);
        User user = UserMapper.toUser(newUser);
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Override
    public void deleteUser(Long adminId, Long userId) {
        checkAdmin(adminId);
        checkUser(userId);
        userRepository.deleteById(userId);
    }

    private void checkUser(Long userId) {
        Optional<User> check = userRepository.findById(userId);
        if (check.isEmpty()) throw new NotFoundException("Пользователь с ID " + userId + " не найден");
    }

    private void checkAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь с ID " + userId + " не найден");
    }
}
