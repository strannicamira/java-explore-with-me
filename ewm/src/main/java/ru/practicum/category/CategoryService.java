package ru.practicum.category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryRequest category);

    List<CategoryDto> findCategoryDtos(Integer from, Integer size);

    CategoryDto findCategoryDtoById(Integer id);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer id);

    void deleteCategoryById(Integer categoryId);
}
