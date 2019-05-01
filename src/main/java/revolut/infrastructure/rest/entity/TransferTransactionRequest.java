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
public class TransferTransactionRequest {
    
    private Integer source;
    private Integer target;
    private BigDecimal amount;
}
