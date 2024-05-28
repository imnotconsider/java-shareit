package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Email;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    @Min(value = 1, message = "id cannot be < 1")
    private final int id;
    private String name;
    @Email(message = "incorrect email")
    private String email;
}
