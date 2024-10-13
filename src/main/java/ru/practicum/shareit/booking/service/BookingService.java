package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, long bookerId);

    BookingDto approveBooking(long bookingId, boolean approved, long userId);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsByOwnerId(long userId);

    List<BookingDto> getBookingsByUserId(long userId);

    Booking getBookingWithUserBookedItem(long itemId, long userId);
}
