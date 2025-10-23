package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto addCategory(Long adminId, NewCategoryDto newCategoryDto);

    void deleteCategory(Long adminId, Long catId);

    CategoryDto updateCategory(Long adminId, Long catId, NewCategoryDto newCategoryDto);
}
