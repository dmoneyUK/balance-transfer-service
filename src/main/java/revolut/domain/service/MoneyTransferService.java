package revolut.domain.service;

import revolut.domain.dto.ProcessResult;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

import java.sql.SQLException;

public interface MoneyTransferService {
    ProcessResult process(MoneyTransferRequest moneyTransferRequest);
}
