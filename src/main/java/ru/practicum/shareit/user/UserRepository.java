package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserRepository {
    User createUser(User user);

    User getUserById(int userId);

    User updateUser(UserDto userDto, int userId);

    void deleteUser(int userId);

    Collection<UserDto> getUsers();
}
