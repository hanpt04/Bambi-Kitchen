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
    public ResponseEntity<Map<String,Object>> getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        CustomUserDetail user = (CustomUserDetail) auth.getPrincipal();
        Map<String,Object> map = new HashMap<>();
        map.put("userid",user.getUserId());
        map.put("username", user.getName());
        map.put("phone", user.getPhone());
        map.put("roles", user.getAuthorities());
        map.put("active", user.isActive());
        return ResponseEntity.ok(map);
    }
    @GetMapping("/my-profile")
    public Map<String, Object> me(@AuthenticationPrincipal CustomOAuth2User user) {
        System.out.println(user.getOauth2User().getName()+"----------");
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("email", user.getAttribute("email"));
        info.put("name", user.getAttribute("name"));
        info.put("roles", user.getAuthorities());
        return info;
    }
    @GetMapping("/debug-principal")
    public Object debug(@AuthenticationPrincipal Object principal) {
        return principal != null ? principal.getClass().getName() : "null";
    }
}
