package ru.practicum.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDto {
    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 2000, message = "Длина комментария должна быть не более {max} символов")
    private String text;
}
