package ru.practicum.events.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.locations.dto.LocationDto;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Длина аннотации события должна быть от {min} до {max} символов")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Длина описания события должна быть от {min} до {max} символов")
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero(message = "Ограничение на количество участников не может быть отрицательным")
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120, message = "Длина названия события должна быть от {min} до {max} символов")
    private String title;
}
