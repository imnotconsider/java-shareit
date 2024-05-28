package ru.practicum.shareit.exception;

public class UserEmailDuplicateException extends RuntimeException {
    public UserEmailDuplicateException(String message) {
        super(message);
    }
}
