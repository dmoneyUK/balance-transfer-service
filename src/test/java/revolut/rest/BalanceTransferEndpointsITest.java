package revolut.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class BalanceTransferEndpointsITest extends JerseyTest {
    
    @Override
    protected Application configure() {
        return new ResourceConfig(BalanceTransferEndpoints.class);
    }
    
    @Test
    public void whenSendingGet_thenMessageIsReturned() throws IOException {
        
        final Response response = target("revolut/balanceTransfer")
                .request()
                .post(Entity.json(""));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        
    }
}

    

