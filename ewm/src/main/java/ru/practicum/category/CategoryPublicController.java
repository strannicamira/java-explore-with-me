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
    public List<CategoryDto> findCategoryDtos(
            @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        List<CategoryDto> categories = categoryService.findCategoryDtos(from, size);
        return categories;
    }

    @GetMapping(value = "/{catId}")
    public CategoryDto findCategoryDtoById(@PathVariable(name = "catId") Integer id) {
        //TODO: handle case for 400, try to use String instead Integer
        CategoryDto categoryDto = categoryService.findCategoryDtoById(id);
        return categoryDto;
    }

}
