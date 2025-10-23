package ru.practicum.categories.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@Validated
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Validated
    public ResponseEntity<CategoryDto> addCategory(@RequestHeader(userIdHeader) Long adminId,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.addCategory(adminId, newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@RequestHeader(userIdHeader) Long adminId, @PathVariable Long catId) {
        categoryService.deleteCategory(adminId, catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestHeader(userIdHeader) Long adminId,
                                                      @PathVariable Long catId,
                                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(adminId, catId, newCategoryDto), HttpStatus.OK);
    }
}
