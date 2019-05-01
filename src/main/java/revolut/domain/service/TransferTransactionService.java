package revolut.domain.service;

import revolut.domain.dto.TransactionResult;
import revolut.infrastructure.rest.entity.TransferTransactionRequest;

public interface TransferTransactionService {
    TransactionResult process(TransferTransactionRequest transferRequest);
}
