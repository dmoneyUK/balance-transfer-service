package revolut.infrastructure.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.domain.service.TransferTransactionService;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static revolut.domain.dto.TransactionResult.DEFAULT_FAILURE;
import static revolut.domain.dto.TransactionResult.SUCCESS;
import static revolut.domain.dto.TransactionResult.UNSUPPORTED;

public class MoneyTransferEndpointsUTest {
    
    private static final String TEST_FAILURE_REASON = "test failure reason";
    private MoneyTransferEndpoints testObject;
    
    @Mock
    private TransferTransactionService transferTransactionServiceMock;
    @Mock
    MoneyTransferRequest requestMock;
    
    @Before
    public void setUp() {
        
        MockitoAnnotations.initMocks(this);
        testObject = new MoneyTransferEndpointsImpl(transferTransactionServiceMock);
    }
    
    @Test
    public void shouldReturn200_whenServiceProcessSuccessfully() {
    
        //Given
        when(transferTransactionServiceMock.process(requestMock)).thenReturn(SUCCESS);
    
        //When
        Response actual = testObject.transfer(requestMock);
    
        //Then
        verify(transferTransactionServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(SUCCESS);
    }
    
    @Test
    public void shouldReturn500AndReason_whenServiceProcessGetDefaultFailure() {
        
        //Given
        when(transferTransactionServiceMock.process(requestMock)).thenReturn(DEFAULT_FAILURE);
        
        //When
        Response actual = testObject.transfer(requestMock);
        
        //Then
        verify(transferTransactionServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(DEFAULT_FAILURE);
    }
    
    @Test
    public void shouldReturn501_whenServiceProcessGotUnsupportedFailured() {
        
        //Given
        when(transferTransactionServiceMock.process(requestMock))
                .thenReturn(UNSUPPORTED);
        
        //When
        Response actual = testObject.transfer(requestMock);
        
        //Then
        verify(transferTransactionServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.NOT_IMPLEMENTED.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(UNSUPPORTED);
    }
    
}
