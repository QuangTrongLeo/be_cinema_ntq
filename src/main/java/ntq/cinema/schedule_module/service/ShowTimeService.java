package ntq.cinema.schedule_module.service;

import lombok.AllArgsConstructor;
import ntq.cinema.schedule_module.repository.ShowTimeRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
}
