package revolut.domain.service;

import revolut.domain.service.dto.ProcessResult;
import revolut.rest.entity.MoneyTransferRequest;

import static revolut.domain.service.dto.ResultType.SUCCESS;

public class MoneyTransferServiceImpl implements MoneyTransferService {
    
    @Override
    public ProcessResult process(MoneyTransferRequest moneyTransferRequest) {
        return ProcessResult.builder().resultType(SUCCESS).build();
    }
}
