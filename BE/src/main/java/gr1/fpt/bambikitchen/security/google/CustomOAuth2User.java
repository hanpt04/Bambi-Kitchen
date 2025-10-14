package gr1.fpt.bambikitchen.security.google;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oauth2User;
    private final int id;
    private final String role;

    public CustomOAuth2User(OAuth2User oauth2User, int id, String role) {
        this.oauth2User = oauth2User;
        this.id = id;
        this.role = role;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attrs = new HashMap<>(oauth2User.getAttributes());
        attrs.put("userId", id);
        return attrs;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("sub");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

}
