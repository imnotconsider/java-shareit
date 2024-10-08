package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends  JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker(User booker);

    Booking findFirstByItemAndBookerAndEndIsBeforeAndStatus(Item item, User user, LocalDateTime end, BookingStatus status);
}