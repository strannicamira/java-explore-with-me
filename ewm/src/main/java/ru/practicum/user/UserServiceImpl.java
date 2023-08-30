package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Search all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User createUser(NewUserRequest userDto) {
        log.info("Create user");
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        return user;
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        log.info("Delete user by id {}", userId);
        userRepository.deleteById(userId);
    }
}
