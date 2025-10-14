package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.LoginRequest;
import gr1.fpt.bambikitchen.security.Mail.MailService;
import gr1.fpt.bambikitchen.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final MailService mailService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Map<String,Object>> getMe(){
        Map<String,Object> map = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        int id = userDetails.getUserId();
        Account account = accountService.findById(id);
        map.put("id", account.getId());
        map.put("name", account.getName());
        map.put("role", account.getRole());
        map.put("phone", account.getPhone());
        map.put("mail", account.getMail());
        return ResponseEntity.ok(map);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try{
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        System.out.println("Trc token");
        String token = jwtUtils.generateToken(auth);
        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(token);}
        catch(BadCredentialsException e){
            throw new CustomException("Invalid password",HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/login-with-google")
    public RedirectView googleLogin() {
        // chuyển hướng sang login bằng gg
        return new RedirectView("/oauth2/authorization/google");
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        mailService.sendOTPMail(email);
        return ResponseEntity.ok("OTP sent to " + email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp, @RequestParam String email, @RequestParam String newPassword) {
        boolean success = passwordResetService.resetPassword(otp, email, newPassword);
        return success
                ? ResponseEntity.ok("Password reset successful")
                : ResponseEntity.badRequest().body("OTP not verified or user not found");
    }
}
