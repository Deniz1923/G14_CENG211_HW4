package game.exceptions;

/**
 * Exception thrown when attempting to use a BoxFixer tool on an already fixed box.
 * <p>
 * A FixedBox cannot be "fixed" again since it's already immovable with a permanent
 * top side. Using BoxFixer on it is an invalid operation.
 * <p>
 * When thrown, the current turn is wasted and the BoxFixer tool is consumed without effect.
 */
public class BoxAlreadyFixedException extends Exception {

    /**
     * Constructs exception with default message.
     */
    public BoxAlreadyFixedException() {
        super("The selected box is already a FixedBox!");
    }

    /**
     * Constructs a new BoxAlreadyFixedException with the specified detail message.
     *
     * @param message the detail message explaining which box was already fixed
     */
    public BoxAlreadyFixedException(String message) {
        super(message);
    }

    /**
     * Constructs a new BoxAlreadyFixedException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public BoxAlreadyFixedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new BoxAlreadyFixedException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception (can be retrieved by Throwable.getCause())
     */
    public BoxAlreadyFixedException(String message, Throwable cause) {
        super(message, cause);
    }
}