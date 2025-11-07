package ru.practicum.categories.mapping;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(null, newCategoryDto.getName());
    }
}
