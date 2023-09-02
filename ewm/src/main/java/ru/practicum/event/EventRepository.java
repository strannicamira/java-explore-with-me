package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventRepository extends JpaRepository<Event, Integer>,
        QuerydslPredicateExecutor<Event>, PagingAndSortingRepository<Event, Integer> {
}
