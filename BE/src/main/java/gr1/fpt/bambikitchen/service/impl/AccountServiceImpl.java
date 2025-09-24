package gr1.fpt.bambikitchen.service.impl;

import gr1.fpt.bambikitchen.exception.CustomException;
import gr1.fpt.bambikitchen.mapper.AccountMapper;
import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;
import gr1.fpt.bambikitchen.repository.AccountRepository;
import gr1.fpt.bambikitchen.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

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
    public Account update(AccountUpdateRequest account) {
        Account oldAccount = accountRepository.findById(account.getId()).orElseThrow(
                () -> new CustomException("Account not found " + account.getId(), HttpStatus.BAD_REQUEST)
        );

        if (!account.getMail().equals(oldAccount.getMail())) {
            throw  new CustomException("Mail is incorrect " + account.getMail(), HttpStatus.BAD_REQUEST);
        }
        Account newAccount = accountMapper.toEntity(account);
        newAccount.setId(oldAccount.getId());

        return accountRepository.save(newAccount);
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
