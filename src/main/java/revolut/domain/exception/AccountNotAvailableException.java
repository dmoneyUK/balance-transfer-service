package revolut.domain.exception;

public class AccountNotAvailableException extends RuntimeException {
    
    public AccountNotAvailableException(String msg) {
        super(msg);
    }
    
}
