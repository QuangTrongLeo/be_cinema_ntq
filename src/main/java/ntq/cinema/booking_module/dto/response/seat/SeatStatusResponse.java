package ntq.cinema.booking_module.dto.response.seat;

import lombok.Builder;
import lombok.Data;
import ntq.cinema.booking_module.enums.SeatStatusEnum;

@Data
@Builder
public class SeatStatusResponse {
    private long statusId;
    private SeatStatusEnum statusName;
}
