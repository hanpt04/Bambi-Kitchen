package gr1.fpt.bambikitchen.schedule;

import gr1.fpt.bambikitchen.model.InventoryOrder;
import gr1.fpt.bambikitchen.security.OTP.OTP;
import gr1.fpt.bambikitchen.security.OTP.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Scheduler  {

    @Autowired
    OTPRepository otpRepository;


    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void terminateExpiredOTPs() {
        List<OTP> expiredOTPs = otpRepository.findAllByCreatedAtBefore(LocalDateTime.now().minusMinutes(5));
        for (OTP otp : expiredOTPs) {
            otp.setExpired(true);
            otpRepository.save(otp);
        }
    }
}
