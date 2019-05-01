package revolut.infrastructure.rest;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import revolut.config.JerseyConfig;
import utils.JsonFixtures;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferTransactionEndpointsFTest extends JerseyTest {
    
    private static final String ENDPOINT_URI = "transactions/transfer";
    
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new JerseyConfig();
    }
    
    @Test
    public void shouldReturn201_whenTransactionIsSuccessful() throws IOException {
    
        final Response response = target(ENDPOINT_URI)
                .request()
                .post(Entity.json(JsonFixtures.read("fixtures/successful_transfer_payload.json")));
        assertThat(response.getStatus()).isEqualTo(200);
    
    }
    
    @Test
    public void shouldReturn404_whenOneAccountNotExist() throws IOException {
        
        final Response response = target(ENDPOINT_URI)
                .request()
                .post(Entity.json(JsonFixtures.read("fixtures/account_not_found_transfer_payload.json")));
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.readEntity(String.class)).isEqualTo("Failed to access to the account: 12345678");
        
    }
    
    @Test
    public void shouldReturn403_whenSourceAccountDoesNotHaveSufficientFund() throws IOException {
        
        final Response response = target(ENDPOINT_URI)
                .request()
                .post(Entity.json(JsonFixtures.read("fixtures/insufficient_fund_transfer_payload.json")));
        
        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.readEntity(String.class)).isEqualTo("No sufficient fund in the account: 22222222");
        
    }
    
    @Test
    public void shouldReturn409_whenTargetAccountBalanceOverLimit() throws IOException {
        
        final Response response = target(ENDPOINT_URI)
                .request()
                .post(Entity.json(JsonFixtures.read("fixtures/account_balance_over_limit_payload.json")));
        assertThat(response.getStatus()).isEqualTo(409);
        
        assertThat(response.readEntity(String.class)).isEqualTo("Transaction failed and rollback was successful.");
        
    }
    
}

    

