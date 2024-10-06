package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.BookingNotAvailableException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingServiceImpl bookingService;

    @Override
    public CommentDto createComment(long itemId, CommentDto commentDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("item with id=%d not found", itemId)));

        Booking booking = bookingService.getBookingWithUserBookedItem(itemId, userId);
        if (booking == null)
            throw new BookingNotAvailableException("you dont have access to this booking");

        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.dtoToComment(commentDto, item, user);

        repository.save(comment);
        return CommentMapper.commentToDto(comment);
    }
}
