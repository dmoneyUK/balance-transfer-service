package revolut.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import revolut.domain.service.TransferTransactionService;
import revolut.domain.service.TransferTransactionServiceImpl;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.persistence.AccountDaoImpl;
import revolut.infrastructure.persistence.H2ConnectionsManager;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    
    @Override
    protected void configure() {
        
        bind(TransferTransactionServiceImpl.class)
                .to(TransferTransactionService.class)
                .in(Singleton.class);;
        bind(new AccountDaoImpl(getConnectionManager()))
                .to(AccountDao.class);
    }
    
    private H2ConnectionsManager getConnectionManager() {
        H2ConnectionsManager connectionManager = H2ConnectionsManager.getInstance();
        connectionManager.populateTestData();
        return connectionManager;
    }
    
}
