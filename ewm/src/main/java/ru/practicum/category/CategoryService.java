package ru.practicum.category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryRequest category);

    List<CategoryDto> findCategories(Integer from, Integer size);

    Category findCategoryById(Integer id);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer id);

    void deleteCategoryById(Integer categoryId);
}
