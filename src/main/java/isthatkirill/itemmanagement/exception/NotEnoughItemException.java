package isthatkirill.itemmanagement.exception;

/**
 * @author Kirill Emelyanov
 */

public class NotEnoughItemException extends RuntimeException {

    public NotEnoughItemException(String message) {
        super(message);
    }
}
