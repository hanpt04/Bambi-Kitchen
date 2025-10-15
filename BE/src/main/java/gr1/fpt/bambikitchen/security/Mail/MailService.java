package gr1.fpt.bambikitchen.security.Mail;

import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.security.OTP.OTPGeneratorService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class MailService {

    private final OTPGeneratorService otpGeneratorService;
    private final ApplicationEventPublisher eventPublisher;

//    public void sendMail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("hungphucvp7@gmail.com");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        mailSender.send(message);
//    }

    public void sendOTPMail(String email) {
        String otp = otpGeneratorService.generateOtpForEmail(email);
        eventPublisher.publishEvent(new EventListenerSystem.SendOTPEvent(email, otp));
    }

    public boolean verifyOTP(String email, String otp) {
        return otpGeneratorService.validateOtp(email, otp);
    }
}
