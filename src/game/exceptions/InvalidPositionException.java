package game.exceptions;

/**
 * Exception thrown when an invalid position is entered or used.
 * This can occur when:
 * - Position coordinates are out of bounds (not 0-7)
 * - Position input string cannot be parsed
 */
public class InvalidPositionException extends RuntimeException {

    /**
     * Constructs an InvalidPositionException with coordinates.
     *
     * @param row The invalid row value
     * @param col The invalid column value
     */
    public InvalidPositionException(int row, int col) {
        super("Invalid position: R" + row + "-C" + col + " is out of bounds (must be 1-8).");
    }

    /**
     * Constructs an InvalidPositionException with an unparseable input string.
     *
     * @param input The invalid input string
     */
    public InvalidPositionException(String input) {
        super("Invalid position format: '" + input + "'. Expected format: R#-C# or #-#");
    }
}
