package game.exceptions;


/**
 * Exception thrown when a player opens a box that contains no SpecialTool.
 * <p>
 * This exception occurs during the second stage of a turn when the selected
 * box is empty. Empty boxes can occur in three ways:
 * 1. RegularBoxes have a 25% chance of being empty (no tool inside)
 * 2. FixedBoxes are always empty (0% chance of containing a tool)
 * 3. Boxes that have been previously opened and their tools were already used
 * <p>
 * When this exception is thrown, the current turn is wasted and the game
 * continues to the next turn without any SpecialTool being acquired or used.
 *
 * @author CENG211 Student
 * @version 1.0
 */
public class EmptyBoxException extends Exception {

    /**
     * Constructs a new EmptyBoxException with no detail message.
     */
    public EmptyBoxException() {
        super("The selected box is empty!");
    }

    /**
     * Constructs a new EmptyBoxException with the specified detail message.
     *
     * @param message the detail message explaining which box was empty
     */
    public EmptyBoxException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmptyBoxException with the specified detail message
     * and cause.
     *
     * @param message the detail message
     * @param cause   the cause of this exception (can be retrieved by Throwable.getCause())
     */
    public EmptyBoxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new EmptyBoxException with the specified cause.
     *
     * @param cause the cause of this exception
     */
    public EmptyBoxException(Throwable cause) {
        super(cause);
    }
}