package revolut.domain.service;

import revolut.infrastructure.persistence.AccountDao;

import javax.inject.Inject;
import java.math.BigDecimal;

public class BalanceTransactionServiceImpl implements BalanceTransactionService {
    private AccountDao accountDao;
    
    @Inject
    public BalanceTransactionServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    
    @Override
    public BigDecimal process(Integer accountNumber) {
        return accountDao.findBy(accountNumber).getBalance();
    }
}
