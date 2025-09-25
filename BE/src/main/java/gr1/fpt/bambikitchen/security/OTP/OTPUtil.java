package gr1.fpt.bambikitchen.security.OTP;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // số 0-9
        }
        return sb.toString();
    }
}
