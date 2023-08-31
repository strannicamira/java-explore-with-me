package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://editor-next.swagger.io")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody NewUserRequest user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> findUsersByIds(@RequestParam(name = "ids", required = false) Integer... ids) {
        //TODO: Add from and size
        return userService.findUsersByIds(ids);
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable(name = "userId") Integer userId) {
        userService.deleteUserById(userId);
    }
}
