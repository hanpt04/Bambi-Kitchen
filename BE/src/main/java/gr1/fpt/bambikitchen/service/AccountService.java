package gr1.fpt.bambikitchen.service;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;

import java.util.List;

public interface AccountService {
    Account save(AccountCreateRequest account);
    Account register(Account account);
    List<Account> findAll();
    Account findById(int id);
//    Account findByUsername(String username);
    Account update(AccountUpdateRequest account);

    String deleteById(int id);
}
