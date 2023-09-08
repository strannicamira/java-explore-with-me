package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptionhandler.NotFoundException;
import ru.practicum.util.ServiceImplUtils;

import java.util.List;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryRequest categoryDto) {
        log.info("[Log][Info] Create category");
        Category category = categoryRepository.save(CategoryMapper.mapToCategory(categoryDto));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findCategoryDtos(Integer from, Integer size) {
        log.info("[Log][Info] Search categories");
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        Iterable<Category> foundCategories = categoryRepository.findAll(page);
        return CategoryMapper.mapToCategoryDto(ServiceImplUtils.mapToList(foundCategories));
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryById(Integer id) {
        log.info("[Log][Info] Search category by id {}", id);
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return foundCategory;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findCategoryDtoById(Integer id) {
        log.info("[Log][Info] Search category dto by id {}", id);
        Category categoryById = findCategoryById(id);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(categoryById);
        return categoryDto;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer id) {
        log.info("[Log][Info] Update category by id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryDto.getName() == null || categoryDto.getName().isBlank() ?
                category.getName() : categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Integer id) {
        log.info("[Log][Info] Delete category by id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        //TODO: 409 Существуют события, связанные с категорией
//        List<Event> events = eventRepository.findAllByCategory(category);
//        if(!events.isEmpty()){
//            throw new EventFoundException("Categories found");
//        }
        categoryRepository.deleteById(id);
    }

}
