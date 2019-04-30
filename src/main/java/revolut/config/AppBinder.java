package revolut.config;

import com.atomikos.jdbc.nonxa.AtomikosNonXADataSourceBean;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import revolut.domain.service.MoneyTransferService;
import revolut.domain.service.MoneyTransferServiceImpl;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.persistence.AccountDaoImpl;
import revolut.infrastructure.persistence.AtomikoDataSource;
import revolut.infrastructure.rest.MoneyTransferEndpoints;
import revolut.infrastructure.rest.MoneyTransferEndpointsImpl;

import javax.inject.Singleton;
import javax.sql.DataSource;

public class AppBinder extends AbstractBinder {
    
    @Override
    protected void configure() {
    
        bind(MoneyTransferEndpointsImpl.class).to(MoneyTransferEndpoints.class).in(Singleton.class);
        bind(MoneyTransferServiceImpl.class).to(MoneyTransferService.class).in(Singleton.class);
        bind(AccountDaoImpl.class).to(AccountDao.class).in(Singleton.class);
        bind(AtomikoDataSource.getInstance()).to(DataSource.class);
        
    }
}
