package revolut.domain.exception;

public class InsufficientFundException extends RuntimeException {
    
    public InsufficientFundException(String msg) {
        super(msg);
    }
    
}
