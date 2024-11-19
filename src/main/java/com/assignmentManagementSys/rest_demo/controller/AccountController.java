package com.assignmentManagementSys.rest_demo.controller;
import com.assignmentManagementSys.rest_demo.entity.Account;
import com.assignmentManagementSys.rest_demo.exceptions.AccountNotFoundException;
import com.assignmentManagementSys.rest_demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

//    @GetMapping("{id}")
//    public Account getAccount(@PathVariable String id) {
//        // Customized exceptions are used for meaningful feedback
//        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
//    }

    @GetMapping("{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
        return ResponseEntity.ok(account);
    }
}
