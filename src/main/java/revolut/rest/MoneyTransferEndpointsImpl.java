package revolut.rest;

import lombok.extern.slf4j.Slf4j;
import revolut.rest.entity.MoneyTransferRequest;
import revolut.service.MoneyTransferService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

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
        
        moneyTransferService.process(moneyTransferRequest);
        return Response.ok("{\"result\": \"success\"}").build();
    }
}
