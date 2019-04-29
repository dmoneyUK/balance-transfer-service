package revolut.infrastructure.persistence;

import revolut.domain.model.AccountDetails;

import java.sql.SQLException;

public interface AccountDao {
    AccountDetails findBy(Integer accountNumber);
}
