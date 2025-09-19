package gr1.fpt.bambikitchen.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String,Object>> auth() {
        Map<String, Object> map = new HashMap<>();
        CustomUserDetail userDetail;
        CustomOAuth2User oauth2User;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        else if(auth.getPrincipal() instanceof CustomUserDetail) {
            userDetail = (CustomUserDetail) auth.getPrincipal();
            map.put("name", userDetail.getName());
            map.put("role", userDetail.getAuthorities());
            map.put("userId",userDetail.getUserId());
        }
        else if(auth.getPrincipal() instanceof CustomOAuth2User) {
            oauth2User = (CustomOAuth2User) auth.getPrincipal();
            map.put("name", oauth2User.getAttribute("name"));
            map.put("role", oauth2User.getAuthorities());
            map.put("userId", oauth2User.getId());
        }
        return ResponseEntity.ok(map);
    }
    @GetMapping("/debug-principal")
    public Object debug(@AuthenticationPrincipal Object principal) {
        return principal != null ? principal.getClass().getName() : "null";
    }
}
