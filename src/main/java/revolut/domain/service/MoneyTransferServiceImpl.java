package revolut.domain.service;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.ProcessResult;
import revolut.domain.model.AccountDetails;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.inject.Inject;

import java.math.BigDecimal;

import static revolut.domain.dto.ResultType.DEFAULT_FAILURE;
import static revolut.domain.dto.ResultType.SUCCESS;
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {
    
    private AccountDao accountDao;
    
    @Inject
    public MoneyTransferServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    
    @Override
    public ProcessResult process(MoneyTransferRequest moneyTransferRequest){
        Integer fromAccountNumber = moneyTransferRequest.getFrom();
        Integer toAccountNumber = moneyTransferRequest.getTo();
    
        AccountDetails fromAccountDetails = null;
        AccountDetails toAccountDetails = null;
        try {
            fromAccountDetails = accountDao.findBy(fromAccountNumber);
            toAccountDetails = accountDao.findBy(toAccountNumber);
    
            BigDecimal fromAccountUpdatedBalance = fromAccountDetails.getBalance().min(moneyTransferRequest.getAmount());
            BigDecimal toAccountUpdatedBalance = toAccountDetails.getBalance().add(moneyTransferRequest.getAmount());
        } catch (Exception e) {
            log.error("Error happened during transfer money from {} to {}. Reason: [{}]", fromAccountNumber, toAccountNumber, e);
            return ProcessResult.builder()
                                .resultType(DEFAULT_FAILURE)
                                .build();
        }
        
        return ProcessResult.builder().resultType(SUCCESS).build();
    }
}
