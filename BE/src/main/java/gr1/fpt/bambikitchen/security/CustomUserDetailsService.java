package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountService accountService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountService.findByPhone(username);
        if (acc == null || !acc.isActive()) {
            throw new UsernameNotFoundException("Not found");
        }
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_"+acc.getRole())
        );
        return new CustomUserDetails(
                acc.getId(),
                acc.getName(),
                acc.getPhone(),
                acc.getPassword(),
                authorities,
                acc.isActive()
        );
    }


}
