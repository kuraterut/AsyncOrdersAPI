package org.kuraterut.mapper;

import org.kuraterut.dto.AccountRequest;
import org.kuraterut.dto.AccountResponse;
import org.kuraterut.model.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public Account toEntity(AccountRequest accountRequest){
        Account account = new Account();
        account.setBalance(accountRequest.getBalance());
        account.setUserId(accountRequest.getUserId());
        return account;
    }

    public AccountResponse toResponse(Account account){
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setUserId(account.getUserId());
        return accountResponse;
    }
}
