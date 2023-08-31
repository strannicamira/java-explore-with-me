package ru.practicum.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.user.User;

public interface CategoryRepository extends JpaRepository<Category, Integer>,
        QuerydslPredicateExecutor<Category>, PagingAndSortingRepository<Category, Integer> {
}
