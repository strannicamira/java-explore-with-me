package ru.practicum.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer>,
        QuerydslPredicateExecutor<Compilation>, PagingAndSortingRepository<Compilation, Integer> {
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
