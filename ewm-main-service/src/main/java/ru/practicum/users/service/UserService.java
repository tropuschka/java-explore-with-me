package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> search(List<Long> ids, int from, int size);
}
