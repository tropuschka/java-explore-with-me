package ru.practicum.categories.mapping;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
