package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.AccountMapper;
import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;
import gr1.fpt.bambikitchen.repository.AccountRepository;
import gr1.fpt.bambikitchen.security.Mail.MailService;
import gr1.fpt.bambikitchen.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public Account save(AccountCreateRequest account) {
        if (accountRepository.existsByMail(account.getMail())) {
            throw new CustomException("Account's email already exists",
                                        HttpStatus.CONFLICT);
        }
        Account newAccount = accountMapper.toEntity(account);
        newAccount.setRole(account.getRole());

        return accountRepository.save(newAccount);
    }

    @Override
    public Account register(Account account) {
        if (accountRepository.existsByMail(account.getMail())) {
            throw new CustomException("Account's email already exists",
                    HttpStatus.CONFLICT);
        }
        if(account.getPhone() == null || account.getPhone().isEmpty()) {
            throw new CustomException("Phone must not be empty !!",HttpStatus.BAD_REQUEST);
        }
        if(accountRepository.existsByPhone(account.getPhone())) {
            throw new CustomException("Account's phone already exists",
                    HttpStatus.CONFLICT);
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(int id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new CustomException("Cannot find account with id " + id,
                                    HttpStatus.BAD_REQUEST)
        );
    }

    @Override
    public Account findByPhone(String phone) {
        if(!accountRepository.existsByPhone((phone))) {
            throw new CustomException("Account not found with " + phone,HttpStatus.BAD_REQUEST);
        }
        return accountRepository.findByPhone(phone);
    }

    @Override
    public Account update(AccountUpdateRequest account) {
        Account oldAccount = accountRepository.findById(account.getId()).orElseThrow(
                () -> new CustomException("Account not found " + account.getId(), HttpStatus.BAD_REQUEST)
        );
        if (accountRepository.existsByMailAndIdNot(account.getMail(),account.getId())) {
            throw new CustomException("Account's email already exists",
                    HttpStatus.CONFLICT);
        }
        if(accountRepository.existsByPhoneAndIdNot(account.getPhone(),account.getId())) {
            throw new CustomException("Account's phone already exists",
                    HttpStatus.CONFLICT);
        }

        // Encode password before updating
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        if (!account.getPassword().equals(oldAccount.getPassword())) sendResetPasswordMessageEvent(account.getMail());

        // Mapping to entity
        Account newAccount = accountMapper.toEntity(account, oldAccount);
        newAccount.setId(oldAccount.getId());

        return accountRepository.save(newAccount);
    }

    @Async
    void sendResetPasswordMessageEvent(String email) {
        log.info("Sending reset password message to {}", email);
        mailService.sendResetPasswordMessageEvent(email);
    }

    @Override
    public Optional<Account> findByEmail(String email) {

        return accountRepository.findByMail(email);
    }

    @Override
    public String deleteById(int id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new CustomException("Cannot find account with id " + id,
                                     HttpStatus.BAD_REQUEST)
        );
        account.setActive(false);
        accountRepository.save(account);
        return "deleted";
    }
}
