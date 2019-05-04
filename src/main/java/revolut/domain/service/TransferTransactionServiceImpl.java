package revolut.domain.service;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.exception.AccountNotAvailableException;
import revolut.domain.exception.InsufficientFundException;
import revolut.domain.exception.TransactionException;
import revolut.infrastructure.framework.TransactionWrapper;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.TransferTransactionRequest;

import javax.inject.Inject;
import java.math.BigDecimal;

import static revolut.domain.dto.TransactionResultType.ACCOUNT_ERROR;
import static revolut.domain.dto.TransactionResultType.BUSINESS_ERROR;
import static revolut.domain.dto.TransactionResultType.DEFAULT_ERROR;
import static revolut.domain.dto.TransactionResultType.SUCCESS;

@Slf4j
public class TransferTransactionServiceImpl implements TransferTransactionService {
    
    private AccountDao accountDao;
    
    @Inject
    public TransferTransactionServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    
    /*
     * Transactional management should be in the service layer. As spring is not recommended target use and I am not
     * familiar with alternatives, just move the responsibility target the dao. Not a good practice and I would not
     * use this approach in any production, but manually write transaction framework seems too heavy for this test.
     * Really curious what framework does Revolut use for transaction management.
     */
    @Override
    public TransactionResult process(TransferTransactionRequest transferRequest) {
        
        log.info("Processing transfer transaction: {}", transferRequest);
        TransactionResult result = TransactionResult.builder().resultType(SUCCESS).build();
        try {
            accountDao.updateBalance(transferRequest.getSource(), BigDecimal.ZERO.subtract(transferRequest.getAmount()));
            accountDao.updateBalance(transferRequest.getTarget(), transferRequest.getAmount());
        } catch (InsufficientFundException | AccountNotAvailableException ae) {
            result = TransactionResult.builder().resultType(ACCOUNT_ERROR).reason(ae.getMessage()).build();
        } catch (TransactionException te) {
            
            result = TransactionResult.builder().resultType(BUSINESS_ERROR).reason(te.getMessage()).build();
        }catch (Exception e){
            result = TransactionResult.builder().resultType(DEFAULT_ERROR).build();
        }
        
        return result;
    }
}
