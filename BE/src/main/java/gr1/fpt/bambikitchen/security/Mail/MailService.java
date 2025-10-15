package gr1.fpt.bambikitchen.security.Mail;

import gr1.fpt.bambikitchen.security.OTP.OTPGeneratorService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final JavaMailSender mailSender;
    private final OTPGeneratorService otpGeneratorService;

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hungphucvp7@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Async("mailPool")
    public void sendOTPMail(String email) {
        String otp = otpGeneratorService.generateOtpForEmail(email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🔑 Your OTP Code (valid 5 minutes)");

            // HTML body
            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                      <h2 style="color: #2c3e50; text-align: center;">🔐 One-Time Password</h2>
                      <p style="font-size: 16px; color: #555;">
                        Please use the following OTP to complete your action:
                      </p>
                      <div style="text-align: center; margin: 20px 0;">
                        <span style="display: inline-block; font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #e74c3c; padding: 10px 20px; border: 2px dashed #e74c3c; border-radius: 8px;">
                          %s
                        </span>
                      </div>
                      <p style="font-size: 14px; color: #888; text-align: center;">
                        This code is valid for <strong>5 minutes</strong>.<br/>
                        If you didn’t request it, please ignore this email.
                      </p>
                    </div>
                    """.formatted(otp);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public boolean verifyOTP(String email, String otp) {
        return otpGeneratorService.validateOtp(email, otp);
    }
}
