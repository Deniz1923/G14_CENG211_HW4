package game.exceptions;

/**
 * Exception thrown when attempting to use a BoxFixer tool on a box that is
 * already a FixedBox.
 * <p>
 * This exception occurs during the second stage of a turn when the player
 * acquires a BoxFixer tool and tries to apply it to a box that is already
 * fixed. Since a FixedBox cannot be rolled in any direction and its top side
 * stays the same at all times, it makes no sense to "fix" an already fixed box.
 * <p>
 * When this exception is thrown, the current turn is wasted and the game
 * continues to the next turn. The BoxFixer tool is consumed but has no effect.
 */
public class BoxAlreadyFixedException extends Exception {

    /**
     * Constructs a new BoxAlreadyFixedException with no detail message.
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
     * Constructs a new BoxAlreadyFixedException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception (can be retrieved by Throwable.getCause())
     */
    public BoxAlreadyFixedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new BoxAlreadyFixedException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public BoxAlreadyFixedException(Throwable cause) {
        super(cause);
    }
}