package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryRequest category) {
        return categoryService.createCategory(category);
    }

    @PatchMapping(value = "/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto,
                                      @PathVariable(name = "catId") Integer id) {
        return categoryService.updateCategory(categoryDto, id);
    }

    @DeleteMapping(value = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable(name = "catId") Integer id) {
        categoryService.deleteCategoryById(id);
    }
}
