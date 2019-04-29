package revolut.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.persistence.AccountDaoImpl;
import revolut.infrastructure.rest.MoneyTransferEndpoints;
import revolut.infrastructure.rest.MoneyTransferEndpointsImpl;
import revolut.domain.service.MoneyTransferService;
import revolut.domain.service.MoneyTransferServiceImpl;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    
    @Override
    protected void configure() {
        
        bind(MoneyTransferEndpointsImpl.class)
                .to(MoneyTransferEndpoints.class)
                .in(Singleton.class);
        bind(MoneyTransferServiceImpl.class)
                .to(MoneyTransferService.class)
                .in(Singleton.class);;
        bind(AccountDaoImpl.class)
                .to(AccountDao.class)
                .in(Singleton.class);
    }
}
