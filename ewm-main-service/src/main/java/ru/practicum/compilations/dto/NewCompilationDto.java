package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String title;
}
