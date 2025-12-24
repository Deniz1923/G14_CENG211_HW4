package game.exceptions;

/**
 * Exception thrown when a player opens a box that contains no SpecialTool.
 * 
 * This exception occurs during the second stage of a turn when the selected
 * box is empty. Empty boxes can occur in three ways:
 *   1. RegularBoxes have a 25% chance of being empty (no tool inside)
 *   2. FixedBoxes are always empty (0% chance of containing a tool)
 *   3. Boxes that have been previously opened (their tools were already used)
 * 
 * When thrown, no tool is acquired for this turn, but the turn is not wasted.
 * The game simply continues without a tool application phase.
 */
public class EmptyBoxException extends Exception {

    /**
     * Constructs exception with default message.
     */
    public EmptyBoxException() {
        super("The selected box is empty!");
    }

    /**
     * Constructs exception with custom message.
     * 
     * @param message detailed explanation of the error
     */
    public EmptyBoxException(String message) {
        super(message);
    }

    /**
     * Constructs exception with a cause.
     * 
     * @param cause the underlying cause of this exception
     */
    public EmptyBoxException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs exception with message and cause.
     * 
     * @param message detailed explanation of the error
     * @param cause   the underlying cause of this exception
     */
    public EmptyBoxException(String message, Throwable cause) {
        super(message, cause);
    }
}