package revolut.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import revolut.rest.entity.MoneyTransferRequest;
import revolut.service.MoneyTransferService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MoneyTransferEndpointsUTest {
    
    private MoneyTransferEndpoints testObject;
    
    @Mock
    private MoneyTransferService moneyTransferServiceMock;
    
    @Before
    public void setUp() {
        
        MockitoAnnotations.initMocks(this);
        testObject = new MoneyTransferEndpointsImpl(moneyTransferServiceMock);
    }
    
    @Test
    public void shouldCallServiceToProcess_whenReceiveValidRequest() {
        MoneyTransferRequest requestMock = mock(MoneyTransferRequest.class);
        testObject.transfer(requestMock);
        
        verify(moneyTransferServiceMock).process(requestMock);
        
    }
}
