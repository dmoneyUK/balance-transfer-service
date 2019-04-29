package revolut.infrastructure.rest;

import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/transactions")
public interface MoneyTransferEndpoints {
    
    @POST
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(MoneyTransferRequest moneyTransferRequest) throws SQLException;
}
