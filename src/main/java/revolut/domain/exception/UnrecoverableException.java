package revolut.domain.exception;


public class UnrecoverableException extends RuntimeException {
    public UnrecoverableException(String s, Exception e) {
        super(s,e);
    }
}
