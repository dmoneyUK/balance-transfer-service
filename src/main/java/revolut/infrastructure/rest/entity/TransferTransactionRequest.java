package revolut.infrastructure.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferTransactionRequest {
    
    @Min(10000000)
    @Max(99999999)
    private int source;
    
    @Min(10000000)
    @Max(99999999)
    private int target;
    
    @DecimalMin(value = "0.00")
    private BigDecimal amount;
}
