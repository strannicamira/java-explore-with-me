package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StatRepository extends JpaRepository<EndpointHitDto, Integer>,
        QuerydslPredicateExecutor<EndpointHitDto>, PagingAndSortingRepository<EndpointHitDto, Integer> {
}
