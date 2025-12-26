package game.util;

import game.exceptions.InvalidPositionException;

/**
 * Immutable class representing a position on the 8x8 grid.
 * Internally uses 0-based indices (0-7), but displays as 1-based (1-8).
 */
public class Position {
    private static final int GRID_SIZE = 8;
    private final int row; // Row index (0-7 internally)
    private final int column; // Column index (0-7 internally)

    /**
     * Constructor for Position.
     *
     * @param row    Row index (0-based, must be 0-7)
     * @param column Column index (0-based, must be 0-7)
     * @throws InvalidPositionException if indices are out of bounds
     */
    public Position(int row, int column) {
        if (row < 0 || row >= GRID_SIZE || column < 0 || column >= GRID_SIZE) {
            throw new InvalidPositionException(row + 1, column + 1);
        }
        this.row = row;
        this.column = column;
    }

    /**
     * Parses a position string in "R#-C#" or "#-#" format (case-insensitive).
     * Converts 1-based input to 0-based internal representation.
     *
     * @param input The input string to parse
     * @return The parsed Position
     * @throws InvalidPositionException if the format is invalid
     */
    public static Position fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidPositionException(input);
        }

        String trimmed = input.trim().toUpperCase();
        int row, col;

        try {
            // Try to parse "R#-C#" format
            if (trimmed.contains("R") && trimmed.contains("C")) {
                // Remove 'R' and split by '-C' or 'C'
                String[] parts = trimmed.replace("R", "").split("-?C");
                if (parts.length != 2) {
                    throw new InvalidPositionException(input);
                }
                row = Integer.parseInt(parts[0].trim()) - 1; // Convert to 0-based
                col = Integer.parseInt(parts[1].trim()) - 1; // Convert to 0-based
            } else if (trimmed.contains("-")) {
                // Try to parse "#-#" format
                String[] parts = trimmed.split("-");
                if (parts.length != 2) {
                    throw new InvalidPositionException(input);
                }
                row = Integer.parseInt(parts[0].trim()) - 1; // Convert to 0-based
                col = Integer.parseInt(parts[1].trim()) - 1; // Convert to 0-based
            } else {
                throw new InvalidPositionException(input);
            }

            // Validate the parsed values
            if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
                throw new InvalidPositionException(row + 1, col + 1); // Show 1-based in error
            }

            return new Position(row, col);
        } catch (NumberFormatException e) {
            throw new InvalidPositionException(input);
        }
    }

    /**
     * Returns the row index (0-based).
     *
     * @return The row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index (0-based).
     *
     * @return The column index
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns a new Position moved one step in the given direction.
     *
     * @param direction The direction to move
     * @return New Position after moving, or null if move would exit grid bounds
     */
    public Position move(Direction direction) {
        int newRow = row + direction.getRowDelta();
        int newCol = column + direction.getColDelta();

        // Check if the new position would be out of bounds
        if (newRow < 0 || newRow >= GRID_SIZE || newCol < 0 || newCol >= GRID_SIZE) {
            return null;
        }

        return new Position(newRow, newCol);
    }

    /**
     * Returns the position in display format "R#-C#" (1-based).
     *
     * @return The display string
     */
    public String toDisplayString() {
        return "R" + (row + 1) + "-C" + (column + 1);
    }

    /**
     * Compares this position with another object for equality.
     * Two positions are equal if they have the same row and column.
     *
     * @param obj The object to compare with
     * @return true if the positions are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }

    /**
     * Returns a hash code for this position.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return 31 * row + column;
    }

    /**
     * Returns a string representation of this position.
     *
     * @return The string representation
     */
    @Override
    public String toString() {
        return toDisplayString();
    }
}
