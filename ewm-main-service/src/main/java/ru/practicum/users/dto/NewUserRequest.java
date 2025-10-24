package ru.practicum.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Электронная почта пользователя должна быть указана")
    @Size(min = 6, max = 254, message = "Длина электронной почты пользователя должна быть от {min} до {max} символов")
    private String email;
    @NotBlank(message = "Имя пользователя должно быть указано")
    @Size(min = 2, max = 250, message = "Длина имени пользователя должна быть от {min} до {max} символов")
    private String name;
}
