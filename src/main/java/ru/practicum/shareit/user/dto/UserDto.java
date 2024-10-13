package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


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
