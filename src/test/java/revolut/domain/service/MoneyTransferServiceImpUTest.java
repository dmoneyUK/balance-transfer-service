package revolut.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.domain.dto.ProcessResult;
import revolut.domain.dto.ResultType;
import revolut.domain.model.AccountDetails;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;
import utils.TestAccount;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class MoneyTransferServiceImpUTest {
    
    private MoneyTransferService testObj;
    @Mock
    private AccountDao accountDaoMock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new MoneyTransferServiceImpl(accountDaoMock);
    }
    
    @Test
    public void shouldThrowTransactionException_whenGetExceptionFromRepository() {
        
        when(accountDaoMock.findBy(anyInt())).thenThrow(RuntimeException.class);
        AccountDetails fromAccount = TestAccount.A.getAccountDetails();
        AccountDetails toAccount = TestAccount.B.getAccountDetails();
        MoneyTransferRequest request = MoneyTransferRequest.builder()
                                                           .from(fromAccount.getAccountNumber())
                                                           .to(toAccount.getAccountNumber())
                                                           .amount(BigDecimal.TEN)
                                                           .build();
        
        ProcessResult actual = testObj.process(request);
        
        assertThat(actual.getResultType()).isEqualTo(ResultType.DEFAULT_FAILURE);
        
    }
}
