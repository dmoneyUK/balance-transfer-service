package revolut.domain.service;

import revolut.domain.service.dto.ProcessResult;
import revolut.rest.entity.MoneyTransferRequest;

public interface MoneyTransferService {
    ProcessResult process(MoneyTransferRequest moneyTransferRequest);
}
