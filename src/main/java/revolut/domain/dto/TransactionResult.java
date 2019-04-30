package revolut.domain.dto;

public enum TransactionResult {
    
    SUCCESS(""),
    UNSUPPORTED("Unsupported Function"),
    DEFAULT_FAILURE("Server error");
    
    private String errorMessage;
    
    TransactionResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
