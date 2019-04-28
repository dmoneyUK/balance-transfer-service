package revolut.domain.service;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.ProcessResult;
import revolut.domain.model.AccountDetails;
import revolut.infrastructure.repositories.AccountRepository;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.inject.Inject;

import java.math.BigDecimal;

import static revolut.domain.dto.ResultType.SUCCESS;
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {
    
    private AccountRepository accountRepository;
    
    @Inject
    public MoneyTransferServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    //TODO implement transactional process
    @Override
    public ProcessResult process(MoneyTransferRequest moneyTransferRequest) {
        Integer fromAccountNumber = moneyTransferRequest.getFrom();
        Integer toAccountNumber = moneyTransferRequest.getTo();
        
        AccountDetails fromAccountDetails = accountRepository.findBy(fromAccountNumber);
        AccountDetails toAccountDetails = accountRepository.findBy(toAccountNumber);
        
        BigDecimal fromAccountUpdatedBalance = fromAccountDetails.getBalance().min(moneyTransferRequest.getAmount());
        BigDecimal toAccountUpdatedBalance = toAccountDetails.getBalance().add(moneyTransferRequest.getAmount());
        
        
        return ProcessResult.builder().resultType(SUCCESS).build();
    }
}
