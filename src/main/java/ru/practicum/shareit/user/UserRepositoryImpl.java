package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserEmailDuplicateException;
import ru.practicum.shareit.exception.UserEmailValidationException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public User createUser(User user) {
        emailValidation(user);
        checkDuplicateEmail(user.getEmail(), id.get() + 1);
        user.setId(id.incrementAndGet());
        users.put(id.get(), user);
        return user;
    }

    @Override
    public User getUserById(int userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("user with id=%d not found", userId));
        }

        return user;
    }

    @Override
    public User updateUser(UserDto userDto, int userId) {
        if (users.get(userId) == null) {
            throw new UserNotFoundException(String.format("user with id=%d not found", userId));
        }

        User user = users.get(userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkDuplicateEmail(userDto.getEmail(), userId);
            user.setEmail(userDto.getEmail());
        }

        users.put(userId, user);
        return users.get(userId);
    }

    @Override
    public void deleteUser(int userId) {
        users.remove(userId);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return users.values().stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }

    private void emailValidation(User user) {
        String userEmail = user.getEmail();
        if (userEmail == null) {
            throw new UserEmailValidationException("email is null");
        }

        if (!userEmail.contains("@")) {
            throw new UserEmailValidationException(String.format("email '%s' haven't '@'", userEmail));
        }
    }

    private void checkDuplicateEmail(String email, int userId) {
        for (User existingUser : users.values()) {
            if (email.equals(existingUser.getEmail()) && existingUser.getId() != userId) {
                throw new UserEmailDuplicateException(String.format("email '%s' is already registered", email));
            }
        }
    }
}
