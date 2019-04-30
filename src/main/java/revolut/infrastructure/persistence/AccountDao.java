package revolut.infrastructure.persistence;

import revolut.domain.model.AccountDetails;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface AccountDao {
    AccountDetails findBy(Integer accountNumber);
    
    int updateAccount(Integer accountNumber, BigDecimal update) throws Exception;
    
}
