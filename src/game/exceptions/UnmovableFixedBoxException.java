package game.exceptions;

/**
 * Exception thrown when attempting to move or flip a FixedBox.
 * <p>
 * This exception is thrown in two scenarios:
 * 1. When a player selects an edge box that is also a FixedBox during the
 * first stage of a turn (rolling phase)
 * 2. When a player attempts to use a BoxFlipper tool on a FixedBox
 * <p>
 * When this exception is thrown, the current turn is wasted and the game
 * continues to the next turn.
 *
 * @author CENG211 Student
 * @version 1.0
 */
public class UnmovableFixedBoxException extends Exception {

    /**
     * Constructs a new UnmovableFixedBoxException with no detail message.
     */
    public UnmovableFixedBoxException() {
        super("Cannot move or flip a FixedBox!");
    }

    /**
     * Constructs a new UnmovableFixedBoxException with the specified detail message.
     *
     * @param message the detail message explaining why the exception was thrown
     */
    public UnmovableFixedBoxException(String message) {
        super(message);
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

    /**
     * Constructs a new UnmovableFixedBoxException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public UnmovableFixedBoxException(Throwable cause) {
        super(cause);
    }
}