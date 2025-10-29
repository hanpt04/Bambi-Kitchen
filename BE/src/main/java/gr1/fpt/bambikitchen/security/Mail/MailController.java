package gr1.fpt.bambikitchen.security.Mail;

import gr1.fpt.bambikitchen.event.EventListenerSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MailController {
    private final MailService mailService;
    private final ApplicationEventPublisher eventPublisher;

//    @GetMapping("/send")
//    public ResponseEntity<String> sendMail() {
//        mailService.sendMail("test@example.com", "Hello", "This is a test email via Mailtrap");
//        return ResponseEntity.ok("send mail successfully");
//    }

    @PostMapping("/send-order-mail")
    public void sendOrderMail(@RequestBody EventListenerSystem.SendOrderEvent request) {
        eventPublisher.publishEvent(new EventListenerSystem.SendOrderEvent(request.email(), request.dishes()));
    }

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        mailService.sendOTPMail(email);
        return ResponseEntity.ok("OTP sent to " + email);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean valid = mailService.verifyOTP(email, otp);
        return valid
                ? ResponseEntity.ok("OTP valid")
                : ResponseEntity.badRequest().body("Invalid or expired OTP");
    }
}
