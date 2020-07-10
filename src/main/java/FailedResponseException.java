public class FailedResponseException extends Exception {
    String code = null;
    String description = null;

    public FailedResponseException(String code, String description) {
        super(code + ": " + description);
        this.code = code;
        this.description = description;
    }

    public FailedResponseException(String errorMessage, String code, String description) {
        super(errorMessage);
        this.code = code;
        this.description = description;
    }

    public FailedResponseException(String errorMessage) {
        super(errorMessage);
    }
}