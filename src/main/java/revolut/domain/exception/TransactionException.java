package revolut.domain.exception;

public class TransactionException extends RuntimeException {
    
    public TransactionException(String msg){
        super(msg);
    }
    
    public TransactionException(Exception e) {
        super(e);
    }
    
    
    public TransactionException(String msg, Exception e) {
        super(msg, e);
    }
}
