package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.validation.Validation;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatDto {
    private Long id;
    @NotBlank(groups = Validation.Create.class)
    private String app;
    @NotBlank(groups = Validation.Create.class)
    private String uri;
    @NotBlank(groups = Validation.Create.class)
    private String ip;
    @NotNull(groups = Validation.Create.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
