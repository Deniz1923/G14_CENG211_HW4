package game.menu;

import game.core.BoxGrid;
import game.exceptions.InvalidPositionException;
import game.util.Direction;

/**
 * Implementation of IValidator for validating user input in the Box Puzzle game.
 * Handles all parsing and rule-based validation logic.
 * Separates validation concerns from input reading (Scanner) operations.
 */
public class InputValidator implements IValidator {

    // Grid size constant (must match BoxGrid.GRID_SIZE)
    private static final int GRID_SIZE = BoxGrid.GRID_SIZE;

    /**
     * Parses a position string in format "R#-C#" or "#-#" to [row, col] array.
     * Supports case-insensitive input as required by specification.
     *
     * @param input the position string (will be converted to uppercase)
     * @return [row, col] as 0-based indices, or null if invalid format
     */
    /**
     * Parses a position string in format "R#-C#" or "#-#" to [row, col] array.
     * Supports case-insensitive input as required by specification.
     *
     * @param input the position string (will be converted to uppercase)
     * @return [row, col] as 0-based indices
     * @throws InvalidPositionException if the format is invalid or position is out of bounds
     */
    @Override
    public int[] parsePosition(String input) throws InvalidPositionException {
        try {
            String normalizedInput = input.trim().toUpperCase();
            String[] parts = normalizedInput.split("-");
            if (parts.length != 2) {
                throw new InvalidPositionException(input);
            }

            int row, col;

            // Format: R1-C5 or r1-c5 (case-insensitive due to toUpperCase above)
            if (parts[0].startsWith("R") && parts[1].startsWith("C")) {
                row = Integer.parseInt(parts[0].substring(1)) - 1;
                col = Integer.parseInt(parts[1].substring(1)) - 1;
            }
            // Format: 1-5 (row-col directly, 1-based)
            else {
                row = Integer.parseInt(parts[0]) - 1;
                col = Integer.parseInt(parts[1]) - 1;
            }

            // Validate bounds - throw exception if invalid
            if (!isValidPosition(row, col)) {
                throw new InvalidPositionException(row + 1, col + 1);
            }

            return new int[]{row, col};
        } catch (NumberFormatException e) {
            // Invalid number in input - throw custom exception
            throw new InvalidPositionException(input);
        }
    }

    /**
     * Validates that a position is within grid bounds.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if position is valid, false otherwise
     */
    @Override
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE;
    }

    /**
     * Validates that a position is on the edge of the grid.
     * Edge positions are: row 0, row 7, column 0, or column 7.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if position is on edge, false otherwise
     */
    @Override
    public boolean isEdgePosition(int row, int col) {
        return row == 0 || row == (GRID_SIZE - 1) || col == 0 || col == (GRID_SIZE - 1);
    }

    /**
     * Validates that the selected direction is valid for a corner box.
     *
     * @param selected        the direction selected by user
     * @param validDirections the two valid directions for this corner
     * @return true if direction is valid, false otherwise
     */
    @Override
    public boolean isValidDirection(Direction selected, Direction[] validDirections) {
        if (selected == null || validDirections == null) return false;
        return selected == validDirections[0] || selected == validDirections[1];
    }

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
    @Override
    public boolean isInRolledPath(Direction rollDirection, int edgeRow, int edgeCol, int selectedRow, int selectedCol) {
        if (rollDirection == Direction.UP || rollDirection == Direction.DOWN) {
            // Rolled vertically - selected box must be in same column
            return selectedCol == edgeCol;
        } else {
            // Rolled horizontally - selected box must be in same row
            return selectedRow == edgeRow;
        }
    }
}
