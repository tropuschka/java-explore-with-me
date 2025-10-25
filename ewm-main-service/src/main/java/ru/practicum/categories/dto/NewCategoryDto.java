package ru.practicum.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Название категории не должно быть пустым")
    @Size(min = 1, max = 50, message = "Длина названия категории должна быть от {min} до {max} символов")
    private String name;
}
