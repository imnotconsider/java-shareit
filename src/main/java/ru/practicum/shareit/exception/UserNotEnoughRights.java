package ru.practicum.shareit.exception;

public class UserNotEnoughRights extends RuntimeException {
    public UserNotEnoughRights(String message) {
        super(message);
    }
}
