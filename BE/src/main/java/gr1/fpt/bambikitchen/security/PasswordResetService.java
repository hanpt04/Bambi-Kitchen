package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.repository.AccountRepository;
import gr1.fpt.bambikitchen.security.OTP.OTPGeneratorService;
import gr1.fpt.bambikitchen.security.OTP.OTPRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPRepository otpRepository;

    public boolean resetPassword(String otp, String email, String newPassword) {
        if (!otpRepository.existsByOtpAndMailAndIsActiveOrderByCreatedAtDesc(otp, email, false)) {
            return false;
        }
        log.info("Reset OTP for {}", email);

        return accountRepository.findByMail(email)
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    accountRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
