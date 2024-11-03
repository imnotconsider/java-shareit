package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.UserEmailDuplicateException;
import ru.practicum.shareit.exception.UserEmailValidationException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (repository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserEmailDuplicateException(String.format("Email '%s' is already registered", userDto.getEmail()));
        }
        return UserMapper.userToDto(repository.save(UserMapper.dtoToUser(userDto)));
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));

        if (StringUtils.hasText(userDto.getName())) {
            user.setName(userDto.getName());
        }
        if (StringUtils.hasText(userDto.getEmail())) {
            emailValidation(userDto.getEmail());
            checkDuplicateEmail(userDto.getEmail(), userId);
            user.setEmail(userDto.getEmail());
        }

        repository.updateUser(userId, user.getName(), user.getEmail());
        return UserMapper.userToDto(user);
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));
        return UserMapper.userToDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        repository.deleteUserById(userId);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return UserMapper.userMapToDto(repository.findAll());
    }

    private void emailValidation(String userEmail) {
        if (!StringUtils.hasText(userEmail)) {
            throw new UserValidationException("email cannot be empty");
        }

        if (!userEmail.contains("@")) {
            throw new UserEmailValidationException(String.format("email '%s' haven't '@'", userEmail));
        }
    }

    private void checkDuplicateEmail(String email, long userId) {
        for (UserDto existingUser : getUsers()) {
            if (email.equals(existingUser.getEmail()) && existingUser.getId() != userId) {
                throw new UserEmailDuplicateException(String.format("email '%s' is already registered", email));
            }
        }
    }
}
