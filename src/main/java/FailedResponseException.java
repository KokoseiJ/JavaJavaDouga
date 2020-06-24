public class FailedResponseException extends Exception {
    public FailedResponseException(String errorMessage){
        super(errorMessage);
    }
}