package org.kuraterut.controller;

import lombok.AllArgsConstructor;
import org.kuraterut.dto.AccountRequest;
import org.kuraterut.dto.AccountResponse;
import org.kuraterut.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<AccountResponse> getAccountByUserId(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

    @PutMapping("/user/add")
    public ResponseEntity<AccountResponse> addMoneyByUserId(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount) {
        return ResponseEntity.ok(accountService.addMoneyByUserId(userId, amount));
    }

    @PutMapping("/user/remove")
    public ResponseEntity<AccountResponse> removeMoneyByUserId(@RequestParam("userId") Long userId, @RequestParam("amount") BigDecimal amount) {
        return ResponseEntity.ok(accountService.removeMoneyByUserId(userId, amount));
    }
}
