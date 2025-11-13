package gr1.fpt.bambikitchen.security.Mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr1.fpt.bambikitchen.Gemini.Gemini;
import gr1.fpt.bambikitchen.Utils.JsonExtractor;
import gr1.fpt.bambikitchen.event.EventListenerSystem;
import gr1.fpt.bambikitchen.model.dto.request.DishNutritionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class MailController {
    private final MailService mailService;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

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

    // Test tính calories bằng Gemini rồi hiển thị kết quả trong trang HTML server-side rendering (cút)
//    @GetMapping(value = "/mail-calculate-calories", produces = MediaType.TEXT_HTML_VALUE)
//    public ResponseEntity<String> calculateCalories(@RequestParam("q") String q) {
//        try {
//            byte[] decoded = Base64.getUrlDecoder().decode(q);
//            DishNutritionRequest request = objectMapper.readValue(decoded, DishNutritionRequest.class);
//            String result = Gemini.roastDish(request);
//
//            String safeName = escapeHtml(request.getName());
//            String safeResp = escapeHtml(JsonExtractor.extractJsonFromGemini(result));
//
//
//            String template = """
//                    <!DOCTYPE html>
//                    <html>
//                    <head>
//                      <meta charset="utf-8">
//                      <meta name="viewport" content="width=device-width,initial-scale=1">
//                      <style>
//                        body{font-family:Segoe UI,Arial,sans-serif;background:#f3f4f6;padding:24px}
//                        .card{max-width:900px;margin:0 auto;background:#fff;padding:18px;border-radius:8px;box-shadow:0 10px 30px rgba(0,0,0,0.08)}
//                        pre{white-space:pre-wrap;word-break:break-word;background:#f8fafc;padding:12px;border-radius:6px;overflow:auto}
//                      </style>
//                    </head>
//                    <body>
//                      <div class="card">
//                        <h2>Nutrition for %NAME%</h2>
//                        <pre>%RESP%</pre>
//                      </div>
//                    </body>
//                    </html>
//                    """;
//
//            String page = template.replace("%NAME%", safeName).replace("%RESP%", safeResp);
//
//            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(page);
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().body("<html><body><h3>Invalid or expired link</h3></body></html>");
//        }
//    }

//     escape HTML special characters to prevent XSS attacks
//    private String escapeHtml(String s) {
//        if (s == null) return "";
//        return s.replace("&", "&amp;")
//                .replace("<", "&lt;")
//                .replace(">", "&gt;")
//                .replace("\"", "&quot;")
//                .replace("'", "&#39;");
//    }
}
