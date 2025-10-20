package gr1.fpt.bambikitchen.security.Mail;

import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.security.OTP.OTPGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class MailService {

    private final OTPGeneratorService otpGeneratorService;
    private final ApplicationEventPublisher eventPublisher;

    public void sendResetPasswordMessageEvent(String to) {
        log.info("Sending reset password message to at MailService {}", to);
        eventPublisher.publishEvent(new EventListenerSystem.SendResetPasswordMessageEvent(to));
    }

    public void sendOTPMail(String email) {
        String otp = otpGeneratorService.generateOtpForEmail(email);
        eventPublisher.publishEvent(new EventListenerSystem.SendOTPEvent(email, otp));
    }

    public boolean verifyOTP(String email, String otp) {
        return otpGeneratorService.validateOtp(email, otp);
    }
}
