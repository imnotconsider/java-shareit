package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private final long id;
    @NotNull(message = "name cannot be empty")
    private String name;
    @Email(message = "incorrect email")
    @NotNull(message = "email cannot be empty")
    private String email;
}
