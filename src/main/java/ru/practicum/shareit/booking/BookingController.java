package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Log4j2
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    @PostMapping()
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Booking controller | createBooking | request: userId({}), bookingDto({})", userId, bookingDto);
        BookingDto booking = bookingService.createBooking(bookingDto, userId);
        log.info("Booking controller | createBooking | answer: created {} ", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId,
                                     @RequestParam Boolean approved) {
        log.info("Booking controller | approveBooking | request: userId({}), bookingId({}), approved({})}", userId, bookingId, approved);
        BookingDto booking = bookingService.approveBooking(bookingId, approved, userId);
        log.info("Booking controller | approveBooking | answer: approved {} ", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        log.info("Booking controller | getBookingById | request: userId({}), bookingId({})", userId, bookingId);
        BookingDto booking = bookingService.getBookingById(userId, bookingId);
        log.info("Booking controller | getBookingById | answer: {} ", booking);
        return booking;
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Booking controller | getBookingsByOwnerId | request: userId({})", userId);
        List<BookingDto> bookings = bookingService.getBookingsByOwnerId(userId);
        log.info("Booking controller | getBookingsByOwnerId | answer: {} ", bookings);
        return bookings;
    }

    @GetMapping
    public List<BookingDto> getBookingsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Booking controller | getBookingsByUserId | request: userId({})", userId);
        List<BookingDto> bookings = bookingService.getBookingsByUserId(userId);
        log.info("Booking controller | getBookingsByUserId | answer: {} ", bookings);
        return bookings;
    }
}
