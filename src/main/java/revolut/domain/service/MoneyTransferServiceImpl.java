package revolut.domain.service;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.model.AccountDetails;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.inject.Inject;

import static revolut.domain.dto.ResultType.SUCCESS;

@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {
    
    private AccountDao accountDao;
    
    @Inject
    public MoneyTransferServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    
    @Override
    public TransactionResult process(MoneyTransferRequest moneyTransferRequest) {
        Integer fromAccountNumber = moneyTransferRequest.getFrom();
        Integer toAccountNumber = moneyTransferRequest.getTo();
        AccountDetails formAccountBalance = accountDao.findBy(fromAccountNumber);
        log.info("From account balance: {}", formAccountBalance);
       
        accountDao.transferBalance(fromAccountNumber, toAccountNumber, moneyTransferRequest.getAmount());
    
        return TransactionResult.builder().resultType(SUCCESS).build();
    }
}
