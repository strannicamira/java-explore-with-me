package ru.practicum.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.category.Category;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>,
        QuerydslPredicateExecutor<Compilation>, PagingAndSortingRepository<Compilation, Integer> {
}
