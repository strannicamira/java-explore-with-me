package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer>,
        QuerydslPredicateExecutor<Request>, PagingAndSortingRepository<Request, Integer> {
    List<Request> findAllByRequesterIdAndEventId(Integer userId, Integer eventId);

    List<Request> findAllByRequesterId(Integer userId);
}
