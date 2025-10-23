package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);
}
