package revolut.infrastructure.rest;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.service.TransferTransactionService;
import revolut.infrastructure.rest.entity.TransferTransactionRequest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
public class TransferTransactionEndpointsImpl implements TransferTransactionEndpoints {
    
    private final TransferTransactionService transferTransactionService;
    
    @Inject
    public TransferTransactionEndpointsImpl(TransferTransactionService transferTransactionService) {
        this.transferTransactionService = transferTransactionService;
    }
    
    @Override
    public Response transfer(TransferTransactionRequest transferRequest) {
        log.info("Received TransferTransactionRequest:{}", transferRequest);
        //Ideally, should convert the request entity target some dto and pass dto target domain.
        //Improve if time allowed.
    
        Status responseStatus = INTERNAL_SERVER_ERROR;
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
}
