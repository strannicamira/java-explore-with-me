package ru.practicum.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LocationRepository extends JpaRepository<Location, Integer>,
        QuerydslPredicateExecutor<Location>, PagingAndSortingRepository<Location, Integer> {
}
