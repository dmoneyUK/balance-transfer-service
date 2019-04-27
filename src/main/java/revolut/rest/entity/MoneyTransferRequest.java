package revolut.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferRequest {
    
    private  AccountInfo from;
    private AccountInfo to;
    private BigDecimal amount;
    private String reference;
}
