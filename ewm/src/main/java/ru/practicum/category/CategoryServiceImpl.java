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
        log.info("Create category");
        Category category = categoryRepository.save(CategoryMapper.mapToCategory(categoryDto));
        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        log.info("Search categories");
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        Iterable<Category> foundCategories = categoryRepository.findAll(page);
        return CategoryMapper.mapToCategoryDto(ServiceImplUtils.mapToList(foundCategories));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findCategoryById(Integer id) {
        log.info("Search category by id {}", id);
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryMapper.mapToCategoryDto(foundCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer id) {
        log.info("Update category by id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryDto.getName() == null || categoryDto.getName().isBlank() ?
                category.getName() : categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.mapToCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Integer id) {
        log.info("Delete category by id {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        //TODO: 409 Существуют события, связанные с категорией
//        List<Event> events = eventRepository.findAllByCategory(category);
//        if(!events.isEmpty()){
//            throw new EventFoundException("Categories found");
//        }
        categoryRepository.deleteById(id);
    }


}
