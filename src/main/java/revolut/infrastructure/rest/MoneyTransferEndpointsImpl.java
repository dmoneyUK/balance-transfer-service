package revolut.infrastructure.rest;

import lombok.extern.slf4j.Slf4j;
import revolut.domain.dto.TransactionResult;
import revolut.domain.service.MoneyTransferService;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_IMPLEMENTED;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
public class MoneyTransferEndpointsImpl implements MoneyTransferEndpoints {
    
    private final MoneyTransferService moneyTransferService;
    
    @Inject
    public MoneyTransferEndpointsImpl(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }
    
    @Override
    public Response transfer(MoneyTransferRequest moneyTransferRequest) {
        log.info("Received MoneyTransferRequest:{}", moneyTransferRequest);
        //Ideally, should convert the request entity to some dto and pass dto to domain.
        //Improve if time allowed.
    
        Status responseStatus = INTERNAL_SERVER_ERROR;
        TransactionResult result = moneyTransferService.process(moneyTransferRequest);
    
        log.info("Money transfer result: {}", result);
        switch (result.getResultType()) {
            case SUCCESS:
                responseStatus = OK;
                break;
            case UNSUPPORTED:
                responseStatus = NOT_IMPLEMENTED;
                break;
        }
    
        return Response.status(responseStatus).entity(result.getReason()).build();
    }
}
