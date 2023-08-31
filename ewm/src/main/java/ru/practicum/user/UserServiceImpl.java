package ru.practicum.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    public List<User> findUsersByIds(Integer... ids) {
        log.info("Search user by ids {}", ids);
        List<Integer> idsList = Arrays.asList(ids);
        BooleanExpression byIds = null;
        if (idsList != null) {
            byIds = QUser.user.id.in(idsList);
        }
        Iterable<User> foundUsers = userRepository.findAll(byIds);
        return new ArrayList<>((Collection) foundUsers);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        log.info("Delete user by id {}", userId);
        userRepository.deleteById(userId);
    }
}
