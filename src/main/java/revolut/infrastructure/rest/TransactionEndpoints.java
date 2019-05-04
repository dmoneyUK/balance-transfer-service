package revolut.infrastructure.rest;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.service.BalanceTransactionService;
import revolut.domain.service.TransferTransactionService;
import revolut.infrastructure.framework.TransactionWrapper;
import revolut.infrastructure.persistence.JpaEntityManagerFactory;
import revolut.infrastructure.rest.entity.TransferTransactionRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
@Path("/transactions")
@Singleton
public class TransactionEndpoints {
    
    private final TransferTransactionService transferTransactionService;
    private final BalanceTransactionService balanceTransactionService;
    
    @Inject
    public TransactionEndpoints(TransferTransactionService transferTransactionService,
                                BalanceTransactionService balanceTransactionService) {
        this.transferTransactionService = TransactionWrapper.transactionally(JpaEntityManagerFactory.getInstance(),transferTransactionService);
        this.balanceTransactionService = balanceTransactionService;
    }
    
    @POST
    @Path("/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid TransferTransactionRequest transferRequest) {
        log.info("Received TransferTransactionRequest:{}", transferRequest);
        //Ideally, should convert the request entity target some dto and pass dto target domain.
        //Improve if time allowed.
        
        Response.Status responseStatus = INTERNAL_SERVER_ERROR;
        TransactionResult result = transferTransactionService.process(transferRequest);
        
        log.info("Money transfer result: {}", result);
        switch (result.getResultType()) {
            case SUCCESS:
                responseStatus = OK;
                break;
            case ACCOUNT_ERROR:
                responseStatus = FORBIDDEN;
                break;
            case BUSINESS_ERROR:
                responseStatus = CONFLICT;
        }
        
        return Response.status(responseStatus).entity(result.getReason()).build();
    }
    
    @GET
    @Path("/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(@QueryParam("accountNumber") int accountNumber) {
        BigDecimal balance = balanceTransactionService.process(accountNumber);
        return Response.ok(balance).build();
    }
    
}
