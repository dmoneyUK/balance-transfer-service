package utils;

import revolut.domain.model.AccountDetails;

import java.math.BigDecimal;

public enum TestAccount {
    A("a", 11111111, BigDecimal.valueOf(1000)), B("b", 22222222, BigDecimal.valueOf(2000));
    
    private final String accountHolder;
    
    private final Integer accountNumber;
    private final BigDecimal balance;
    
    TestAccount(String accountHolder, Integer accountNumber, BigDecimal balance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    
    public AccountDetails getAccountDetails() {
        return AccountDetails.builder()
                             .accountHolder(accountHolder)
                             .accountNumber(accountNumber)
                             .balance(balance)
                             .build();
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public String getAccountHolder() {
        return accountHolder;
    }
    
    public Integer getAccountNumber() {
        return accountNumber;
    }
}
