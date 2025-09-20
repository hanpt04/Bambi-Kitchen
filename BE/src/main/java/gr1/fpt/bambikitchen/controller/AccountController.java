package gr1.fpt.bambikitchen.controller;

import gr1.fpt.bambikitchen.model.Account;
import gr1.fpt.bambikitchen.model.dto.request.AccountCreateRequest;
import gr1.fpt.bambikitchen.model.dto.request.AccountUpdateRequest;
import gr1.fpt.bambikitchen.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> save(@RequestBody AccountCreateRequest account){
        return ResponseEntity.ok(accountService.save(account));
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll(){
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable Integer id){
        return ResponseEntity.ok(accountService.findById(id));
    }

    @PutMapping
    public ResponseEntity<Account> update(@RequestBody AccountUpdateRequest request){
        return ResponseEntity.ok(accountService.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        return ResponseEntity.ok(accountService.deleteById(id));
    }
}
