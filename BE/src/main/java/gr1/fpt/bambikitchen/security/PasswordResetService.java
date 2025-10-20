package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.repository.AccountRepository;
import gr1.fpt.bambikitchen.security.Mail.MailService;
import gr1.fpt.bambikitchen.security.OTP.OTPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPRepository otpRepository;
    private final MailService mailService;

    public boolean resetPassword(String otp, String email, String newPassword) {
        if (!otpRepository.existsByOtpAndMailAndIsActiveOrderByCreatedAtDesc(otp, email, false)) {
            return false;
        }
        log.info("Reset OTP for {}", email);

        boolean isUpdated = accountRepository.findByMail(email)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    accountRepository.save(user);
                    return true;
                })
                .orElse(false);

        // Send mail to user
        sendMail(email, isUpdated);

        return isUpdated;
    }

    @Async
     void sendMail(String email, boolean isUpdated) {
        if (isUpdated) mailService.sendResetPasswordMessageEvent(email);
    }
}
