package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingDto bookingToDto(Booking booking, Item item, User booker) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.itemToDto(item))
                .booker(UserMapper.userToDto(booker))
                .status(booking.getStatus())
                .build();
    }

    public static BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.itemToDto(booking.getItem()))
                .booker(UserMapper.userToDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static Booking dtoToBooking(BookingDto bookingDto, Item item, User booker) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(bookingDto.getStatus() == null ? BookingStatus.WAITING : bookingDto.getStatus())
                .build();
    }
}
