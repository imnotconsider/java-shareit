package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, int userId);

    UserDto getUserById(int userId);

    void deleteUser(int userId);

    Collection<UserDto> getUsers();
}
