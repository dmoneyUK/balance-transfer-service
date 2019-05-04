package revolut.domain.service;

import java.math.BigDecimal;

public interface BalanceTransactionService {
    
    BigDecimal process(Integer accountNumber);
}
