package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> findCategories(
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size) {
        List<CategoryDto> categories = categoryService.findCategories(from, size);
        return categories;
    }

    @GetMapping(value = "/{catId}")
    public CategoryDto findCategoryByIds(@PathVariable(name = "catId") Integer id) {
        //TODO: handle case for 400, try to use String instead Integer
        Category categoryById = categoryService.findCategoryById(id);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(categoryById);
        return categoryDto;
    }

}
