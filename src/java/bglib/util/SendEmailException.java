package bglib.util;

public class SendEmailException extends Exception {
    public SendEmailException(String msg, Exception e) {
        super(msg, e);
    }
    public SendEmailException(Exception e) {
        super(e);
    }
}
