package gr1.fpt.bambikitchen.security.Mail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping("/send")
    public ResponseEntity<String> sendMail() {
        mailService.sendMail("test@example.com", "Hello", "This is a test email via Mailtrap");
        return ResponseEntity.ok("send mail successfully");
    }

//    @GetMapping("/send-otp")
//    public ResponseEntity<String> sendOTP(@RequestParam String email) {
//        mailService.sendOTPMail(email);
//        return ResponseEntity.ok("OTP sent to " + email);
//    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = mailService.verifyOTP(email, otp);
        return valid
                ? ResponseEntity.ok("OTP valid")
                : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }
}
