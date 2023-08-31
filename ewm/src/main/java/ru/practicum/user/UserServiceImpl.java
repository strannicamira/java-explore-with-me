package ru.practicum.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.util.ServiceImplUtils;

import java.util.Arrays;
import java.util.List;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

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
        Pageable page = ServiceImplUtils.getPage(from, size, SORT_BY_ID_ASC);
        BooleanExpression byIds = null;
        if (ids != null) {
            List<Integer> idsList = Arrays.asList(ids);
            byIds = QUser.user.id.in(idsList);
        }
        Iterable<User> foundUsers = userRepository.findAll(byIds, page);
        return ServiceImplUtils.mapToList(foundUsers);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        log.info("Delete user by id {}", userId);
        userRepository.deleteById(userId);
    }
}
