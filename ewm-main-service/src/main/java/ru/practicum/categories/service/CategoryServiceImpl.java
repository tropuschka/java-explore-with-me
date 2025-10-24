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
import ru.practicum.exceptions.*;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from, size));
        return categories.stream().map(CategoryMapper::toDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        ApiError apiError;
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty()) {
            throw new NotFoundException("Категория с id " + catId + " не найдена");
        }
        return CategoryMapper.toDto(category.get());
    }

    @Override
    public CategoryDto addCategory(Long adminId, NewCategoryDto newCategoryDto) {
        checkAdmin(adminId);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        Optional<Category> checkUniqueName = categoryRepository.findByName(category.getName());
        if (checkUniqueName.isPresent()) {
            throw new ConflictException("Категория \"" + category.getName() + "\" уже добавлена");
        }
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public void deleteCategory(Long adminId, Long catId) {
        checkAdmin(adminId);
        checkCategory(catId);
        List<Event> categoryEvents = eventRepository.findByCategoryId(catId);
        if (!categoryEvents.isEmpty()) {
            throw new ConflictException("Нельзя удалить категорию, в которой есть события");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long adminId, Long catId, NewCategoryDto newCategoryDto) {
        checkAdmin(adminId);
        Optional<Category> checkUnique = categoryRepository.findByName(newCategoryDto.getName());
        if (checkUnique.isPresent() && !checkUnique.get().getId().equals(catId)) {
            throw new ConflictException("Категория \"" + newCategoryDto.getName() + "\" уже добавлена");
        }

        Category category = checkCategory(catId);
        category.setName(newCategoryDto.getName());
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDto(saved);
    }

    // Пока просто проверка на существующего пользователя. Добавить ли поле is_admin или отдельную таблицу с админами?
    private void checkAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) throw new NotFoundException("Пользователь не найден");
    }

    private Category checkCategory(Long catId) {
        Optional<Category> categoryOpt = categoryRepository.findById(catId);
        if (categoryOpt.isEmpty()) throw new NotFoundException("Категория не найдена");
        return categoryOpt.get();
    }
}
