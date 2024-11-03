package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(long itemId, CommentDto commentDto, long userId);
}
