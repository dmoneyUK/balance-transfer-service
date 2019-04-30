package revolut.infrastructure.persistence;

import revolut.domain.exception.TransationException;
import revolut.domain.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    Account findBy(Integer AccountNumber) throws TransationException;
    
    void transferBalance(Integer fromAccount, Integer toAccount, BigDecimal amount) throws TransationException;;
}
