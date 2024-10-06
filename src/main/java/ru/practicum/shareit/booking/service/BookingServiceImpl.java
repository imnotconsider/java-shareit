package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, long bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", bookerId)));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(String.format("item with id=%d not found", bookingDto.getItemId())));

        if (!item.getAvailable())
            throw new ItemNotAvailableException(String.format("item-%d is not available",  item.getId()));

        if (bookingDto.getStart().equals(bookingDto.getEnd()))
            throw new BookingDateException("start date after end date or start date equals end date");
        Booking booking = BookingMapper.dtoToBooking(bookingDto, item, booker);
        booking.setItem(item);
        repository.save(booking);
        return BookingMapper.bookingToDto(booking, item, booker);
    }

    @Override
    public BookingDto approveBooking(long bookingId, boolean approved, long userId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("booking with id=%d not found", bookingId)));

        if (booking.getItem().getOwner().getId() != userId)
            throw new UserNotEnoughRights("you dont have access to this booking");

        if (approved)
            booking.setStatus(BookingStatus.APPROVED);
        else
            booking.setStatus(BookingStatus.REJECTED);

        repository.save(booking);
        return BookingMapper.bookingToDto(booking);
    }

    @Override
    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.format("booking with id=%d not found", bookingId)));
        if (!(booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId))
            throw new UserNotEnoughRights("you dont have access to this booking");
        return BookingMapper.bookingToDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));
        return repository.findAllByBooker(owner).stream()
                .map(BookingMapper::bookingToDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsByUserId(long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));
        return repository.findAllByBooker(owner).stream()
                .map(BookingMapper::bookingToDto)
                .toList();
    }

    @Override
    public Booking getBookingWithUserBookedItem(long itemId, long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("item with id=%d not found", itemId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", userId)));

        return repository.findFirstByItemAndBookerAndEndIsBeforeAndStatus(item, user, LocalDateTime.now(), BookingStatus.APPROVED);
    }
}
