package revolut.infrastructure.repositories;

import revolut.domain.model.AccountDetails;

import java.sql.SQLException;

public interface AccountRepository {
    AccountDetails findBy(Integer accountNumber);
}
