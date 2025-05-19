package ntq.cinema.booking_module.service;

import ntq.cinema.booking_module.dto.request.seat.SeatCreateRequest;
import ntq.cinema.booking_module.dto.request.seat.SeatUpdateNameRequest;
import ntq.cinema.booking_module.dto.request.seat.SeatUpdatePriceRequest;
import ntq.cinema.booking_module.dto.request.seat.SeatUpdateStatusRequest;
import ntq.cinema.booking_module.dto.response.seat.SeatResponse;
import ntq.cinema.booking_module.entity.Seat;
import ntq.cinema.booking_module.entity.SeatStatus;
import ntq.cinema.booking_module.enums.SeatStatusEnum;
import ntq.cinema.booking_module.mapper.SeatMapper;
import ntq.cinema.booking_module.repository.SeatRepository;
import ntq.cinema.booking_module.repository.SeatStatusRepository;
import ntq.cinema.schedule_module.entity.Room;
import ntq.cinema.schedule_module.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    private final int seatPrice = 50000;

    private final SeatRepository seatRepository;
    private final SeatStatusRepository seatStatusRepository;
    private final RoomRepository roomRepository;
    private final SeatMapper seatMapper;
    public SeatService(SeatRepository seatRepository,
                       SeatStatusRepository seatStatusRepository,
                       RoomRepository roomRepository,
                       SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.seatStatusRepository = seatStatusRepository;
        this.roomRepository = roomRepository;
        this.seatMapper = seatMapper;
    }

    // LẤY THÔNG TIN CHỖ NGỒI BẰNG ID
    public SeatResponse getSeatById(long seatId){
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chỗ ngồi!"));
        return seatMapper.mapperToResponse(seat);
    }

    // TẠO CHỖ NGỒI
    public SeatResponse createSeat(SeatCreateRequest request){
        SeatStatus availableStatus = seatStatusRepository.findByName(SeatStatusEnum.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Trạng thái AVAILABLE không tồn tại"));
        Room room = roomRepository.findById(request.getRoomId()).orElse(null);
        Seat seat = new Seat();
        seat.setRoom(room);
        seat.setName(request.getRoomName());
        seat.setPrice(request.getRoomPrice());
        seat.setStatus(availableStatus);
        seat  = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // TẠO CÁC CHỖ NGỒI CHO PHÒNG CHIẾU
    public void createSeatsForRoom(Room room){
        SeatStatus availableStatus = seatStatusRepository.findByName(SeatStatusEnum.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Trạng thái AVAILABLE không tồn tại"));

        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row <= 'J'; row++) {
            for (int col = 1; col <= 16; col++){
                String seatName = String.format("%c%02d", row, col);
                Seat seat = new Seat();
                seat.setRoom(room);
                seat.setName(seatName);
                seat.setPrice(seatPrice); // Giả định giá mặc định
                seat.setStatus(availableStatus);
                seats.add(seat);
            }
        }

        List<Seat> saveSeats = seatRepository.saveAll(seats);
        seatMapper.mapperToResponseList(saveSeats);
    }

    // CẬP NHẬT TÊN CHỖ NGỒI
    public SeatResponse updateSeatName(SeatUpdateNameRequest request){
        Seat seat = findSeatById(request.getSeatId());
        seat.setName(request.getSeatName());
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // CẬP NHẬT GIÁ CHỖ NGỒI
    public SeatResponse updateSeatPrice(SeatUpdatePriceRequest request){
        Seat seat = findSeatById(request.getSeatId());
        seat.setPrice(request.getSeatPrice());
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI (ADMIN)
    public SeatResponse updateSeatStatus(SeatUpdateStatusRequest request){
        Seat seat = findSeatById(request.getSeatId());
        SeatStatus seatStatus = seatStatusRepository.findById(request.getSeatStatusId())
                        .orElseThrow(() -> new RuntimeException("Trạng thái của chỗ ngồi không tồn tại!"));
        if (seat.getStatus() == seatStatus){
            throw new RuntimeException("Chỗ ngồi đang cùng trạng thái!");
        }
        seat.setStatus(seatStatus);
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // XÓA CHỖ NGỒI
    public void deleteSeat(long seatId){
        Seat seat = findSeatById(seatId);
        seatRepository.delete(seat);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI AVAILABLE CHO USER(SELECTING -> AVAILABLE)
    public SeatResponse updateSeatStatusAvailableForUser(long seatId){
        Seat seat = findSeatById(seatId);
        SeatStatus seatStatus = seatStatusRepository.findByName(SeatStatusEnum.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Trạng thái AVAILABLE không tồn tại!"));
        if (seat.getStatus() == seatStatus){
            throw new RuntimeException("Chỗ ngồi đang cùng trạng thái AVAILABLE");
        }
        if (seat.getStatus().getName().equals(SeatStatusEnum.BOOKED)){
            throw new RuntimeException("Không thể cập nhật lại trạng thái AVAILABLE");
        }
        seat.setStatus(seatStatus);
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI SELECTING CHO USER (AVAILABLE -> SELECTING)
    public SeatResponse updateSeatStatusSelectingForUser(long seatId){
        Seat seat = findSeatById(seatId);
        SeatStatus seatStatus = seatStatusRepository.findByName(SeatStatusEnum.SELECTING)
                .orElseThrow(() -> new RuntimeException("Trạng thái SELECTING không tồn tại!"));
        if (seat.getStatus() == seatStatus){
            throw new RuntimeException("Chỗ ngồi đang cùng trạng thái SELECTING");
        }
        seat.setStatus(seatStatus);
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // CẬP NHẬT TRẠNG THÁI CHỖ NGỒI BOOKED CHO CUSTOMER (SELECTING -> CUSTOMER)
    public SeatResponse updateSeatStatusBookedForCus(long seatId){
        Seat seat = findSeatById(seatId);
        SeatStatus seatStatus = seatStatusRepository.findByName(SeatStatusEnum.BOOKED)
                .orElseThrow(() -> new RuntimeException("Trạng thái BOOKED không tồn tại!"));
        if (seat.getStatus() == seatStatus){
            throw new RuntimeException("Chỗ ngồi đang cùng trạng thái BOOKED");
        }
        seat.setStatus(seatStatus);
        seat = seatRepository.save(seat);
        return seatMapper.mapperToResponse(seat);
    }

    // Tìm chỗ ngồi bằng id
    private Seat findSeatById(long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chỗ ngồi!"));
    }

}
