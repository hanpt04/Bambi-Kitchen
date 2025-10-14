package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account save(AccountCreateRequest account);
    Account register(Account account);
    List<Account> findAll();
    Account findById(int id);
    Account findByPhone(String username);
    Account update(AccountUpdateRequest account);
    Optional<Account> findByEmail(String email);
    String deleteById(int id);
}
