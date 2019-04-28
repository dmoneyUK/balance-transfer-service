package revolut.infrastructure.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import revolut.config.JerseyConfig;
import utils.JsonFixtures;

import javax.swing.table.TableStringConverter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTransferEndpointsFTest extends JerseyTest {
    
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new JerseyConfig();
    }
    
    @Test
    public void shouldReturn201_whenPost() throws IOException {
        
        final Response response = target("transactions/transfer")
                .request()
                .post(Entity.json(JsonFixtures.read("fixtures/valid_transfer_payload.json")));
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        
    }
}

    

