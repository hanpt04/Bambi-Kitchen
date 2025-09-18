package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepository.findByPhone(username);

        if (acc == null) {
            throw new UsernameNotFoundException("User not found");
        }

        if(!acc.isActive()){
            throw new UsernameNotFoundException("Account is inactive");
        }

        return new CustomUserDetail(acc.getId(),
                acc.getName(),
                acc.getPhone(),
                acc.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_"+acc.getRole())),
                acc.isActive());
    }
}
