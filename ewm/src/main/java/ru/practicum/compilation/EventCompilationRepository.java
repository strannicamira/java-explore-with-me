package ru.practicum.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Integer>,
        QuerydslPredicateExecutor<EventCompilation>, PagingAndSortingRepository<EventCompilation, Integer> {
//    List<Integer> findAllByCompilationId(Integer compilationId);

    List<EventCompilation> findAllByCompilationId(Integer id);
}
