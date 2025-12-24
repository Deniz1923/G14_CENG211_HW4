package game.exceptions;

/**
 * Exception thrown when attempting to move or flip a FixedBox.
 * 
 * This exception is thrown in two scenarios:
 *   1. When a player selects an edge FixedBox during the rolling phase (first stage)
 *   2. When a player attempts to use a BoxFlipper tool on a FixedBox
 * 
 * When thrown during Stage 1 (rolling), the turn is wasted and the game skips
 * to the next turn. When thrown during tool usage, only the tool is wasted.
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
     * Constructs exception with a cause.
     * 
     * @param cause the underlying cause of this exception
     */
    public UnmovableFixedBoxException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs exception with message and cause.
     * 
     * @param message detailed explanation of the error
     * @param cause   the underlying cause of this exception
     */
    public UnmovableFixedBoxException(String message, Throwable cause) {
        super(message, cause);
    }
}