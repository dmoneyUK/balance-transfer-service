package revolut.infrastructure.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.domain.dto.ProcessResult;
import revolut.domain.dto.ResultType;
import revolut.domain.service.MoneyTransferService;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoneyTransferEndpointsUTest {
    
    private static final String TEST_FAILURE_REASON = "test failure reason";
    private MoneyTransferEndpoints testObject;
    
    @Mock
    private MoneyTransferService moneyTransferServiceMock;
    
    @Before
    public void setUp() {
        
        MockitoAnnotations.initMocks(this);
        testObject = new MoneyTransferEndpointsImpl(moneyTransferServiceMock);
    }
    
    @Test
    public void shouldReturn200_whenServiceProcessSuccessfully() {
    
        //Given
        MoneyTransferRequest requestMock = mock(MoneyTransferRequest.class);
        ProcessResult processResult = ProcessResult.builder()
                                                   .resultType(ResultType.SUCCESS)
                                                   .build();
        when(moneyTransferServiceMock.process(requestMock)).thenReturn(processResult);
    
        //When
        Response actual = testObject.transfer(requestMock);
    
        //Then
        verify(moneyTransferServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(processResult.getReason());
    }
    
    @Test
    public void shouldReturn500AndReason_whenServiceProcessGetDefaultFailure() {
        
        //Given
        MoneyTransferRequest requestMock = mock(MoneyTransferRequest.class);
        ProcessResult processResult = ProcessResult.builder()
                                                   .resultType(ResultType.DEFAULT_FAILURE)
                                                   .reason(TEST_FAILURE_REASON)
                                                   .build();
        when(moneyTransferServiceMock.process(requestMock)).thenReturn(processResult);
        
        //When
        Response actual = testObject.transfer(requestMock);
        
        //Then
        verify(moneyTransferServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(TEST_FAILURE_REASON);
    }
    
    @Test
    public void shouldReturn501_whenServiceProcessGotUnsupportedFailured() {
        
        //Given
        MoneyTransferRequest requestMock = mock(MoneyTransferRequest.class);
        when(moneyTransferServiceMock.process(requestMock))
                .thenReturn(ProcessResult.builder()
                                         .resultType(ResultType.UNSUPPORTED)
                                         .reason(TEST_FAILURE_REASON)
                                         .build());
        
        //When
        Response actual = testObject.transfer(requestMock);
        
        //Then
        verify(moneyTransferServiceMock).process(requestMock);
        assertThat(actual.getStatus()).isEqualTo(Response.Status.NOT_IMPLEMENTED.getStatusCode());
        assertThat(actual.getEntity()).isEqualTo(TEST_FAILURE_REASON);
    }
    
}
