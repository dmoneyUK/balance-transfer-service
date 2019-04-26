package revolut.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/revolut")
public class BalanceTransferEndpoints {
    
    @POST
    @Path("/balanceTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer() {
        return Response.ok("{\"result\": \"success\"}").build();
    }
}
