package ru.practicum.user;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User createUser(NewUserRequest user);
    List<User> findUsersByIds(Integer... ids);
    void deleteUserById(Integer userId);
}
