package ntq.cinema.booking_module.mapper;

import lombok.AllArgsConstructor;
import ntq.cinema.booking_module.dto.response.booking.BookingResponse;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.booking_module.entity.BookingSeat;
import ntq.cinema.booking_module.entity.Seat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final SeatMapper seatMapper;

    public BookingResponse mapperToResponse(Booking booking) {
        List<SeatResponse> seatResponses = booking.getBookingSeats().stream()
                .map(BookingSeat::getSeat)
                .map(seatMapper::mapperToResponse)
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUser().getUserId())
                .showTimeId(booking.getShowTime().getShowtimeId())
                .createdAt(booking.getCreatedAt())
                .bookedSeats(seatResponses)
                .build();
    }

    public List<BookingResponse> mapperToResponseList(List<Booking> seats) {
        return seats.stream()
                .map(this::mapperToResponse)
                .collect(Collectors.toList());
    }
}
