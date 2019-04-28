package revolut.infrastructure.repositories;

import revolut.domain.model.AccountDetails;

public interface AccountRepository {
    AccountDetails findBy(Integer AccountNumber);
}
