package ru.practicum.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.locations.dto.LocationDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Аннотация события должна быть указана")
    @Size(min = 20, max = 2000, message = "Длина аннотации события должна быть от {min} до {max} символов")
    private String annotation;
    @NotNull(message = "Категория события должна быть указана")
    private Long category;
    @NotBlank(message = "Описание события должно быть указано")
    @Size(min = 20, max = 7000, message = "Длина описания события должна быть от {min} до {max} символов")
    private String description;
    @NotBlank(message = "Дата события должна быть указана")
    private String eventDate;
    @NotNull(message = "Место проведения события должно быть указано")
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero(message = "Ограничение на количество участников не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(message = "Название события должно быть указано")
    @Size(min = 3, max = 120, message = "Длина названия события должна быть от {min} до {max} символов")
    private String title;
}
