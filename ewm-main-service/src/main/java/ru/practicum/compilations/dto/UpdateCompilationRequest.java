package ru.practicum.compilations.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Длина названия подборки должна быть от {min} до {max} символов")
    private String title;
}
