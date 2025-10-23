package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapping.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from, size));
        return categories.stream().map(CategoryMapper::toDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) throw new NotFoundException("Категория не найдена");
        return CategoryMapper.toDto(category.get());
    }
}
