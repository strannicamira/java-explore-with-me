package ru.practicum.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User mapToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

}
