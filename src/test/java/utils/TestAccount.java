package utils;

import revolut.domain.model.AccountDetails;

import java.math.BigDecimal;

public enum TestAccount {
    A( 11111111,"a", BigDecimal.valueOf(1000)), B( 22222222,"b", BigDecimal.valueOf(2000));
    
    private final Integer accountNumber;
    private final String accountHolder;
    private final BigDecimal balance;
    
    TestAccount( Integer accountNumber, String accountHolder,BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
    
    public AccountDetails getAccountDetails() {
        return AccountDetails.builder()
                             .accountNumber(accountNumber)
                             .accountHolder(accountHolder)
                             .balance(balance)
                             .build();
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
}
