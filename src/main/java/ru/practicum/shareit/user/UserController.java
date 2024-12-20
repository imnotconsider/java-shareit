package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Log4j2
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        log.info("user-{} created", user);
        return user;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable("userId") long userId) {
        UserDto user = userService.updateUser(userDto, userId);
        log.info("user-{} updated to {}", userId, userDto);
        return user;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") long userId) {
        UserDto user = userService.getUserById(userId);
        log.info("item-{} requested: {}", userId, user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
        log.info("user-{} deleted", userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("user list requested.");
        return userService.getUsers();
    }
}
