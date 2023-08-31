package ru.practicum.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.practicum.user.Constants.PAGE_SIZE;
import static ru.practicum.user.Constants.SORT_BY_ID_ASC;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(NewUserRequest userDto) {
        log.info("Create user");
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findUsersByIds(Integer from, Integer size, Integer... ids) {
        log.info("Search user by ids {}", ids);
        Pageable page = getPage(from, size, SORT_BY_ID_ASC);
        BooleanExpression byIds = null;
        if (ids != null) {
            List<Integer> idsList = Arrays.asList(ids);
            byIds = QUser.user.id.in(idsList);
        }
        Iterable<User> foundUsers = userRepository.findAll(byIds, page);
        return mapToList(foundUsers);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        log.info("Delete user by id {}", userId);
        userRepository.deleteById(userId);
    }

    private Pageable getPage(Integer from, Integer size, Sort sort) {
        Pageable page = PageRequest.of(0, PAGE_SIZE, sort);
        if (from != null && size != null) {

            if (from < 0 || size <= 0) {
                throw new IllegalStateException("Not correct page parameters");
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size, sort);
        }
        return page;
    }

    public static <T> List<T> mapToList(Iterable<T> obgs) {
        List<T> list = new ArrayList<>();
        for (T obg : obgs) {
            list.add(obg);
        }
        return list;
    }
}
