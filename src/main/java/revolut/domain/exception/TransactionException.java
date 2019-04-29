package revolut.domain.exception;

public class TransactionException extends RuntimeException {
    public TransactionException(Exception e) {
        super(e);
    }
}
