package ntq.cinema.auth_module.util;

import java.security.SecureRandom;

public class RandomOtpUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // sinh số ngẫu nhiên từ 100000 đến 999999
        return String.valueOf(otp);
    }
}
