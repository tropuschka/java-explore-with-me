package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> search(Long adminId, List<Long> ids, int from, int size);

    UserDto addUser(Long adminId, NewUserRequest newUserRequest);

    void deleteUser(Long adminId, Long id);
}
