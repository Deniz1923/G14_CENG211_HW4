package game.menu;

import game.util.Direction;

/**
 * Interface for validating user input in the Box Puzzle game.
 * Separates validation logic from input reading concerns.
 * Implementations handle parsing and rule-based validation.
 */
public interface IValidator {

    /**
     * Parses a position string in format "R#-C#" or "#-#" to [row, col] array.
     *
     * @param input the position string
     * @return [row, col] as 0-based indices
     * @throws game.exceptions.InvalidPositionException if the format is invalid or
     *                                                  position is out of bounds
     */
    int[] parsePosition(String input);

    /**
     * Validates that a position is within grid bounds.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if position is valid, false otherwise
     */
    boolean isValidPosition(int row, int col);

    /**
     * Validates that a position is on the edge of the grid.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if position is on edge, false otherwise
     */
    boolean isEdgePosition(int row, int col);

    /**
     * Validates that the selected direction is valid for a corner box.
     *
     * @param selected        the direction selected by user
     * @param validDirections the two valid directions for this corner
     * @return true if direction is valid, false otherwise
     */
    boolean isValidDirection(Direction selected, Direction[] validDirections);

    /**
     * Validates that a box selection is in the rolled row or column.
     *
     * @param rollDirection the direction of the roll
     * @param edgeRow       the row of the edge box that initiated rolling
     * @param edgeCol       the column of the edge box that initiated rolling
     * @param selectedRow   the row of the selected box
     * @param selectedCol   the column of the selected box
     * @return true if selection is in the rolled row/column, false otherwise
     */
    boolean isInRolledPath(Direction rollDirection, int edgeRow, int edgeCol, int selectedRow, int selectedCol);
}
