package org.kuraterut.service;

import lombok.AllArgsConstructor;
import org.kuraterut.dto.AccountRequest;
import org.kuraterut.dto.AccountResponse;
import org.kuraterut.exception.exceptions.AccountNotFoundException;
import org.kuraterut.mapper.AccountMapper;
import org.kuraterut.model.entity.Account;
import org.kuraterut.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = accountMapper.toEntity(accountRequest);
        account = accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public AccountResponse updateAccount(Long id, AccountRequest accountRequest) {
        if(!accountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found by id: " + id);
        }
        Account account = accountMapper.toEntity(accountRequest);
        account.setId(id);
        account = accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public void deleteAccount(Long id) {
        if(!accountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found by id: " + id);
        }
        accountRepository.deleteById(id);
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found by id: " + id));
        return accountMapper.toResponse(account);
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> accountMapper.toResponse(account)).toList();
    }

    public AccountResponse getAccountByUserId(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found by user id: " + userId));
        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse addMoneyByUserId(Long userId, BigDecimal amount) {
        if(!accountRepository.existsByUserId(userId)) {
            throw new AccountNotFoundException("Account not found by user id: " + userId);
        }
        accountRepository.addMoneyByUserId(userId, amount);
        return getAccountByUserId(userId);
    }

    @Transactional
    public AccountResponse removeMoneyByUserId(Long userId, BigDecimal amount) {
        if(!accountRepository.existsByUserId(userId)) {
            throw new AccountNotFoundException("Account not found by user id: " + userId);
        }
        accountRepository.removeMoneyByUserId(userId, amount);
        return getAccountByUserId(userId);
    }
}
