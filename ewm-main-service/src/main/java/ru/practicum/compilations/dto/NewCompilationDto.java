package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewCompilationDto {
    private Set<Long> events = new HashSet<>();
    private boolean pinned;
    @NotBlank(message = "Название подборки не должно быть пустым")
    @Size(min = 1, max = 50, message = "Длина названия подборки должна быть от {min} до {max} символов")
    private String title;
}
