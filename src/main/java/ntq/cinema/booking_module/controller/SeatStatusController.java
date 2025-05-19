package ntq.cinema.booking_module.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.ntq-cinema-url}/seats-status")
@RequiredArgsConstructor
public class SeatStatusController {
}
