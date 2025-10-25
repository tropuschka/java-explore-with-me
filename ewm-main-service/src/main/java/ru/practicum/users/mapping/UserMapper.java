package ru.practicum.users.mapping;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

public class UserMapper {
    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static User toUser(NewUserRequest userRequest) {
        return new User(null, userRequest.getName(), userRequest.getEmail());
    }
}
