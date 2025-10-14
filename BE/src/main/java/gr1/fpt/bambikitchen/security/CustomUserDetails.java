package gr1.fpt.bambikitchen.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Data
public class CustomUserDetails implements UserDetails {
    private final int userId;
    private final String name;
    private final String phone;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public CustomUserDetails(int userId, String name, String phone , String password, Collection<? extends GrantedAuthority> authorities, boolean active) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    public CustomUserDetails(int userId, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.name = null;
        this.phone = null;
        this.password = null;
        this.authorities = authorities;
        this.active = true;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
