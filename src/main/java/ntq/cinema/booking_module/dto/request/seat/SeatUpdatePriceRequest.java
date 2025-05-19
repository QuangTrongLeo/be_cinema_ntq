package ntq.cinema.booking_module.dto.request.seat;

import lombok.Data;

@Data
public class SeatUpdatePriceRequest {
    private long seatId;
    private int seatPrice;
}
