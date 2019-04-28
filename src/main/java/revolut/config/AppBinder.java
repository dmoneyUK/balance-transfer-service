package revolut.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import revolut.rest.MoneyTransferEndpoints;
import revolut.rest.MoneyTransferEndpointsImpl;
import revolut.domain.service.MoneyTransferService;
import revolut.domain.service.MoneyTransferServiceImpl;

public class AppBinder extends AbstractBinder {
    
    @Override
    protected void configure() {
        
        bind(MoneyTransferEndpointsImpl.class).to(MoneyTransferEndpoints.class);
        bind(MoneyTransferServiceImpl.class).to(MoneyTransferService.class);
    }
}
