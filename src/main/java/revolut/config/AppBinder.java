package revolut.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import revolut.domain.service.BalanceTransactionService;
import revolut.domain.service.BalanceTransactionServiceImpl;
import revolut.domain.service.TransferTransactionService;
import revolut.domain.service.TransferTransactionServiceImpl;
import revolut.infrastructure.framework.TransactionalUnitOfWorkRunner;
import revolut.infrastructure.framework.UnitOfWorkRunner;
import revolut.infrastructure.persistence.AccountDao;
import revolut.infrastructure.persistence.AccountDaoImpl;
import revolut.infrastructure.persistence.JpaEntityManagerFactory;

import javax.inject.Singleton;

public class AppBinder extends AbstractBinder {
    
    @Override
    protected void configure() {
        
        bind(TransferTransactionServiceImpl.class)
                .to(TransferTransactionService.class)
                .in(Singleton.class);
        bind(BalanceTransactionServiceImpl.class)
                .to(BalanceTransactionService.class)
                .in(Singleton.class);
        bind(new AccountDaoImpl(JpaEntityManagerFactory.getInstance()))
                .to(AccountDao.class);
        //bind(new TransactionalUnitOfWorkRunner(JpaEntityManagerFactory.getInstance())).to(UnitOfWorkRunner.class);
    }
    
}
