package ru.practicum.user;

import java.util.List;

public interface UserService {
    User createUser(NewUserRequest user);

    List<User> findUsersByIds(Integer from, Integer size, Integer[] ids);

    void deleteUserById(Integer userId);

}
