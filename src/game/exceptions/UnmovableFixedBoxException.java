package game.exceptions;

/**
 * Exception thrown when attempting to move or flip a FixedBox.
 * <p>
 * This exception is thrown in two scenarios:
 * - When a player selects an edge FixedBox during the rolling phase (first stage)
 * - When a player attempts to use a BoxFlipper tool on a FixedBox
 * <p>
 * When thrown, the current turn is wasted and the game continues to the next turn.
 */
public class UnmovableFixedBoxException extends Exception {

    /**
     * Constructs exception with default message.
     */
    public UnmovableFixedBoxException() {
        super("Cannot move or flip a FixedBox!");
    }

    /**
     * Constructs exception with custom message.
     *
     * @param message detailed explanation of the error
     */
    public UnmovableFixedBoxException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnmovableFixedBoxException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public UnmovableFixedBoxException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnmovableFixedBoxException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception (can be retrieved by Throwable.getCause())
     */
    public UnmovableFixedBoxException(String message, Throwable cause) {
        super(message, cause);
    }
}