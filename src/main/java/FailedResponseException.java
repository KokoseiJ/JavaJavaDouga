public class FailedResponseException extends Exception {
    int code = 0;
    String description = null;

    public FailedResponseException(String errorMessage, int code, String description) {
        super(errorMessage);
        this.code = code;
        this.description = description;
    }

    public FailedResponseException(String errorMessage, String code, String description) {
        super(errorMessage);
        this.code = Integer.parseInt(code);
        this.description = description;
    }

    public FailedResponseException(String errorMessage) {
        super(errorMessage);
    }
}