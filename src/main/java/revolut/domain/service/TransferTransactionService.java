package revolut.domain.service;

import revolut.domain.dto.TransactionResult;
import revolut.infrastructure.rest.entity.MoneyTransferRequest;

public interface TransferTransactionService {
    TransactionResult process(MoneyTransferRequest moneyTransferRequest);
}
