package revolut.domain.exception;

public class TransationException extends RuntimeException {
    
    public TransationException(String msg) {
        super(msg);
    }
    
    public TransationException(String msg, Exception e) {
        super(msg, e);
    }
}
