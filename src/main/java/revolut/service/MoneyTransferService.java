package revolut.service;

import revolut.rest.entity.MoneyTransferRequest;

public interface MoneyTransferService {
    void process(MoneyTransferRequest moneyTransferRequest);
}
