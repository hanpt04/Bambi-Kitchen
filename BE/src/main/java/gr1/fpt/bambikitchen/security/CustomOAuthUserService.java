package gr1.fpt.bambikitchen.security;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.enums.Role;
import gr1.fpt.bambikitchen.repository.AccountRepository;
import gr1.fpt.bambikitchen.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    public Account linkAccount (String mail, String name){
        Optional<Account> existAcc =accountRepository.findByMail(mail);
        Account acc;
        if(existAcc.isPresent()){
            acc=existAcc.get();
        }
        else{
            acc=new Account();
            acc.setMail(mail);
            acc.setName(name);
            acc.setRole(Role.USER);
            acc.setActive(true);
            accountRepository.save(acc);
        }
        return acc;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate =new DefaultOAuth2UserService();
        OAuth2User u = delegate.loadUser(userRequest);
        Map<String, Object> attributes = u.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Account account = linkAccount(email, name);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRole().toString()));


        DefaultOAuth2User defaultUser = new DefaultOAuth2User(authorities, attributes, "sub");

        return new CustomOAuth2User(defaultUser,account.getId(),account.getRole().toString());
    }
}
