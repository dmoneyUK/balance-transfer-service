package revolut.domain.service;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.model.Account;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.inject.Inject;

import static revolut.domain.dto.TransactionResult.SUCCESS;

@Slf4j
public class TransferTransactionTransactionServiceImpl implements TransferTransactionService {
    
    private AccountDao accountDao;
    
    @Inject
    public TransferTransactionTransactionServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    
    @Override
    public TransactionResult process(MoneyTransferRequest transferRequest) {
        
        log.info("Processing transfer transaction: {}", transferRequest);
        accountDao.transferBalance(transferRequest.getFrom(), transferRequest.getTo(), transferRequest.getAmount());
    
        return SUCCESS;
    }
}
