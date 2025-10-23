package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.mapping.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.ConditionsNotMetException;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

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

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        Optional<Category> checkUniqueName = categoryRepository.findByName(category.getName());
        if (checkUniqueName.isPresent()) throw new ConditionsNotMetException("Такая категория уже добавлена");
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public void deleteCategory(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) throw new NotFoundException("Категория не найдена");
        List<Event> categoryEvents = eventRepository.findByCategoryId(catId);
        if (!categoryEvents.isEmpty()) {
            throw new ConditionsNotMetException("Нельзя удалить категорию, в которой есть события");
        }
        categoryRepository.deleteById(catId);
    }
}
