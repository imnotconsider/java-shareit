package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, long userId);

    UserDto getUserById(long userId);

    void deleteUser(long userId);

    Collection<UserDto> getUsers();
}
