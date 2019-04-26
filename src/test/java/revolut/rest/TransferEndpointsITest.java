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

public class TransferEndpointsITest extends JerseyTest {
    
    @Override
    protected Application configure() {
        return new ResourceConfig(TransferEndpoints.class);
    }
    
    @Test
    public void shouldReturn201_whenPost() throws IOException {
        
        final Response response = target("revolut/transfer")
                .request()
                .post(Entity.json(""));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        
    }
}

    

