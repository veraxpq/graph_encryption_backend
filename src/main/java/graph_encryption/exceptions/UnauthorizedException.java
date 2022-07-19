package graph_encryption.exceptions;

/**
 * This class represents an unauthorized exception.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * This method set the error message.
     */
    public UnauthorizedException() {
        super("Please login!");
    }

    /**
     * This method set the error message with the given string.
     *
     * @param message given error message
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
