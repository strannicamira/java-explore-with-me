package ru.practicum.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>,
        QuerydslPredicateExecutor<Comment>, PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findAllByEventId(Integer eventId, Pageable page);

    List<Comment> findAllByEventId(Integer eventId);
}
