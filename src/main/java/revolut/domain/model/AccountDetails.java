package revolut.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
public class AccountDetails {
    
    private final String accountHolder;
    private final String sortCode;
    private final String accountNumber;
    private final BigDecimal balance;
}
