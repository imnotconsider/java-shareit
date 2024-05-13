package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.dtoToUser(userDto);
        return UserMapper.userToDto(repository.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, int userId) {
        return UserMapper.userToDto(repository.updateUser(userDto, userId));
    }

    @Override
    public UserDto getUserById(int userId) {
        return UserMapper.userToDto(repository.getUserById(userId));
    }

    @Override
    public void deleteUser(int userId) {
        repository.deleteUser(userId);
    }

    @Override
    public Collection<UserDto> getUsers() {
        return repository.getUsers();
    }
}
