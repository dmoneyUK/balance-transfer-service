package revolut.domain.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.domain.dto.TransactionResult;
import revolut.domain.exception.AccountNotAvailableException;
import revolut.domain.exception.InsufficientFundException;
import revolut.domain.exception.TransactionException;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.rest.entity.TransferTransactionRequest;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static revolut.domain.dto.TransactionResultType.ACCOUNT_ERROR;
import static revolut.domain.dto.TransactionResultType.BUSINESS_ERROR;
import static revolut.domain.dto.TransactionResultType.DEFAULT_ERROR;
import static revolut.domain.dto.TransactionResultType.SUCCESS;
import static utils.TestAccount.A;
import static utils.TestAccount.B;

public class TransferTransactionServiceImpUTest {
    
    private static final String ERROR_SAMPLE = "error sample";
    private static final Integer FROM_ACCOUNT = A.getAccountNumber();
    private static final Integer TO_ACCOUNT = B.getAccountNumber();
    private static final BigDecimal TX_AMOUNT = TEN;
    private static final TransferTransactionRequest transferRequest = TransferTransactionRequest.builder()
                                                                                                .source(FROM_ACCOUNT)
                                                                                                .target(TO_ACCOUNT)
                                                                                                .amount(TX_AMOUNT)
                                                                                                .build();
    
    private TransferTransactionService testObj;
    
    @Mock
    private AccountDao accountDaoMock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new TransferTransactionServiceImpl(accountDaoMock);
    }
    
    @Test
    public void shouldReturnSuccess_whenRepositoryTransferBalanceWasSuccessful() {
    
        TransactionResult actual = testObj.process(transferRequest);
    
        verify(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        assertThat(actual.getResultType()).isEqualTo(SUCCESS);
    }
    
    @Test
    public void shouldReturnAccountError_whenDaoTransferBalanceGetAccountNotAvailableException() {
        
        doThrow(new AccountNotAvailableException(ERROR_SAMPLE))
                .when(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        
        TransactionResult actual = testObj.process(transferRequest);
        
        verify(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        assertThat(actual.getResultType()).isEqualTo(ACCOUNT_ERROR);
        assertThat(actual.getReason()).isEqualTo(ERROR_SAMPLE);
    }
    
    @Test
    public void shouldReturnAccountError_whenDaoTransferBalanceGetInsufficientFundException() {
        
        doThrow(new InsufficientFundException(ERROR_SAMPLE))
                .when(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        
        TransactionResult actual = testObj.process(transferRequest);
        
        verify(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        assertThat(actual.getResultType()).isEqualTo(ACCOUNT_ERROR);
        assertThat(actual.getReason()).isEqualTo(ERROR_SAMPLE);
    }
    
    @Test
    public void shouldReturnBusinessError_whenDaoTransferBalanceGetTransactionException() {
        
        doThrow(new TransactionException(ERROR_SAMPLE))
                .when(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        
        TransactionResult actual = testObj.process(transferRequest);
        
        verify(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        assertThat(actual.getResultType()).isEqualTo(BUSINESS_ERROR);
        assertThat(actual.getReason()).isEqualTo(ERROR_SAMPLE);
    }
    
    @Test
    public void shouldReturnDefaultFailure_whenDaoTransferBalanceGetRuntimeException() {
        
        doThrow(new RuntimeException())
                .when(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        
        TransactionResult actual = testObj.process(transferRequest);
        
        verify(accountDaoMock).transferBalance(FROM_ACCOUNT, TO_ACCOUNT, TX_AMOUNT);
        assertThat(actual.getResultType()).isEqualTo(DEFAULT_ERROR);
    }
}
