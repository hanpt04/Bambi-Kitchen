package gr1.fpt.bambikitchen.security.OTP;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OTPGeneratorService {

    private final OTPRepository otpRepository;

    public String generateOtpForEmail(String email) {
        return saveOTP(email);
    }

    public boolean validateOtp(String email, String otp) {
        OTP entry = otpRepository.findFirstByMailOrderByCreatedAtDesc(email);
        if (entry == null) {
            return false;
        }
        if (!entry.getOtp().equals(otp)) {
            return false;
        }
        if (entry.isExpired()) {
            return false;
        }
        entry.setActive(false);
        otpRepository.save(entry);
        return true;
    }

    private String saveOTP(String email) {
        OTP newOtp = new OTP();
        while (true) {
            String otp = OTPUtil.generateOtp(6);
            if (!otpRepository.existsById(OTPUtil.generateOtp(6))) {
                newOtp.setOtp(otp);
                newOtp.setMail(email);
                otpRepository.save(newOtp);
                return otp;
            }
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void terminateExpiredOTPs() {
        List<OTP> expiredOTPs = otpRepository.findAllByCreatedAtBefore(LocalDateTime.now().minusMinutes(5));
        for (OTP otp : expiredOTPs) {
            otp.setExpired(true);
            otpRepository.save(otp);
        }
        log.info("Terminated {} expired OTPs", expiredOTPs.size());
    }
}
