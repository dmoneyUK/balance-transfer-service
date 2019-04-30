package revolut.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.domain.dto.TransactionResult;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static revolut.domain.dto.TransactionResult.SUCCESS;
import static utils.TestAccount.A;
import static utils.TestAccount.B;

public class TransferTransactionServiceImpUTest {
    
    private TransferTransactionService testObj;
    @Mock
    private AccountDao accountDaoMock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new TransferTransactionTransactionServiceImpl(accountDaoMock);
    }
    
    @Test
    public void shouldReturnSuccess_whenRepositoryTransferBalanceWasSuccessful() {
        Integer fromAccount = A.getAccountNumber();
        Integer toAccount = B.getAccountNumber();
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                                                           .from(fromAccount)
                                                           .to(toAccount)
                                                           .amount(BigDecimal.TEN)
                                                           .build();
    
        TransactionResult actual = testObj.process(request);
        verify(accountDaoMock).transferBalance(fromAccount, toAccount, request.getAmount());
    
        assertThat(actual).isEqualTo(SUCCESS);
    }
}
