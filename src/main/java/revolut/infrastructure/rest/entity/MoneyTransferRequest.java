package revolut.infrastructure.rest.entity;

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
    
    private Integer from;
    private Integer to;
    private BigDecimal amount;
}
