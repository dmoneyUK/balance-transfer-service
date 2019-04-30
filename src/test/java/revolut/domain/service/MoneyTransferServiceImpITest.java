package revolut.domain.service;

import org.junit.Before;
import org.junit.Test;
import revolut.domain.model.AccountDetails;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.persistence.AccountDaoImpl;
import revolut.infrastructure.persistence.AtomikoDataSource;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;
import utils.TestAccount;

import java.math.BigDecimal;

public class MoneyTransferServiceImpITest {
    
    private MoneyTransferService testObj;
    
    private AccountDao accountDao;
    
    @Before
    public void setUp() {
        this.accountDao = new AccountDaoImpl(AtomikoDataSource.getInstance());
        testObj = new MoneyTransferServiceImpl(accountDao);
    }
    
    @Test
    public void shouldReturnFailedAndNoAccountChange_whenOneAccountChangeFailed() {
        AccountDetails fromAccount = TestAccount.A.getAccountDetails();
        AccountDetails toAccount = TestAccount.B.getAccountDetails();
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                                                           .from(fromAccount.getAccountNumber())
                                                           .to(toAccount.getAccountNumber())
                                                           .amount(BigDecimal.valueOf(500))
                                                           .build();
        
        testObj.process(request);
        
        
        System.out.println("from account:" + accountDao.findBy(fromAccount.getAccountNumber()));
        System.out.println("to account:" + accountDao.findBy(toAccount.getAccountNumber()));
        
    }
}
