package ntq.cinema.auth_module.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VerifyOtpRequest {
    private String email;
    private String otpCode;
}
