package game.exceptions;

/**
 * Exception thrown when attempting to use a BoxFixer tool on an already fixed box.
 * 
 * A FixedBox cannot be "fixed" again since it's already immovable with a permanent
 * top side. Using BoxFixer on it is an invalid operation.
 * 
 * When thrown, the tool is consumed but has no effect. The turn continues normally
 * (the player doesn't lose the turn, only the tool is wasted).
 */
public class BoxAlreadyFixedException extends Exception {

    /**
     * Constructs exception with default message.
     */
    public BoxAlreadyFixedException() {
        super("The selected box is already a FixedBox!");
    }

    /**
     * Constructs exception with custom message.
     * 
     * @param message detailed explanation of the error
     */
    public BoxAlreadyFixedException(String message) {
        super(message);
    }

    /**
     * Constructs exception with a cause.
     * 
     * @param cause the underlying cause of this exception
     */
    public BoxAlreadyFixedException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs exception with message and cause.
     * 
     * @param message detailed explanation of the error
     * @param cause   the underlying cause of this exception
     */
    public BoxAlreadyFixedException(String message, Throwable cause) {
        super(message, cause);
    }
}