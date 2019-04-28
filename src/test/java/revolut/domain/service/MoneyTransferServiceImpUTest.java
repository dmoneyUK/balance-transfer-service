package revolut.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.infrastructure.repositories.AccountRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class MoneyTransferServiceImpUTest {
    
    private MoneyTransferService testObj;
    @Mock
    private AccountRepository accountRepositoryMock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new MoneyTransferServiceImpl(accountRepositoryMock);
    }
    
    @Test
    public void shouldMoveMoneyBetweenAccounts() {
        //AccountInfo fromAccount = A.getAccountInfo();
        //AccountInfo toAccount = B.getAccountInfo();
        //MoneyTransferRequest request = MoneyTransferRequest.builder()
        //                                                   .from(fromAccount)
        //                                                   .to(toAccount)
        //                                                   .amount(BigDecimal.TEN)
        //                                                   .reference("a to b")
        //                                                   .build();
        //
        //ProcessResult actual = testObj.process(request);
        //verify(accountRepositoryMock).findBy( fromAccount.getAccountNumber());
        //verify(accountRepositoryMock).findBy(toAccount.getAccountNumber());
        //
        //assertThat(actual.getResultType()).isEqualTo(ResultType.SUCCESS);
    }
}
