package ntq.cinema.booking_module.service;

import lombok.AllArgsConstructor;
import ntq.cinema.auth_module.entity.User;
import ntq.cinema.auth_module.repository.UserRepository;
import ntq.cinema.booking_module.dto.request.booking.BookingCreateRequest;
import ntq.cinema.booking_module.dto.response.booking.BookingResponse;
import ntq.cinema.booking_module.entity.Booking;
import ntq.cinema.booking_module.entity.BookingSeat;
import ntq.cinema.booking_module.entity.Seat;
import ntq.cinema.booking_module.mapper.BookingMapper;
import ntq.cinema.booking_module.repository.BookingRepository;
import ntq.cinema.payment_module.entity.Payment;
import ntq.cinema.payment_module.enums.PaymentStatusEnum;
import ntq.cinema.schedule_module.entity.ShowTime;
import ntq.cinema.schedule_module.repository.ShowTimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatService seatService;
    private final BookingMapper bookingMapper;

    public BookingResponse createBookingForUser(BookingCreateRequest request) {
        // Lấy user và shotime trong DB
        User user = findUserById(request.getUserId());
        ShowTime showTime = findShowTimeById(request.getShowTimeId());

        // Lấy danh sách ghế
        List<Seat> seats = seatService.getSeats(request.getSeatIds());

        // Tạo Booking và danh sách BookingSeat
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowTime(showTime);

        List<BookingSeat> bookingSeats = buildBookingSeats(seats, booking);
        booking.setBookingSeats(bookingSeats);

        // Tính tổng tiền
        int totalAmount = totalAmountOfBooking(seats);

        // Tạo Payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentGateway("VNPAY");
        payment.setStatus(PaymentStatusEnum.PENDING);
        booking.setPayment(payment);

        // Lưu Booking vào DB
        bookingRepository.save(booking);

        return bookingMapper.mapperToResponse(booking);
    }

    // TÌM USER
    private User findUserById(long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));
    }

    // TÌM GIỜ CHIẾU CỦA PHIM
    private ShowTime findShowTimeById(long showTimeId){
        return showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ShowTime của phim!"));
    }

    // BUILD BOOKING SEAT
    private List<BookingSeat> buildBookingSeats(List<Seat> seats, Booking booking){
        return seats.stream()
                .map(seat -> {
                    BookingSeat bs = new BookingSeat();
                    bs.setSeat(seat);
                    bs.setBooking(booking);
                    return bs;
                }).collect(Collectors.toList());
    }

    // TÍNH TỔNG TIỀN CỦA BOOKING
    private int totalAmountOfBooking(List<Seat> seats){
        return seats.stream()
                .mapToInt(Seat::getPrice)
                .sum();
    }

}
