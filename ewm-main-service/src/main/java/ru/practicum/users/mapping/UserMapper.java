package ru.practicum.users.mapping;

import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

public class UserMapper {
    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
