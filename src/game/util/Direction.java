package game.util;

/**
 * Enum representing the four cardinal directions for box rolling.
 * 
 * Using an enum instead of String literals provides:
 *   - Type safety (compiler catches invalid directions)
 *   - Better IDE support (auto-completion, refactoring)
 *   - Cleaner code with clear intent
 * 
 * Each direction has an associated row delta and column delta
 * for calculating movement on the grid.
 */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    // Row offset when moving in this direction
    private final int rowDelta;
    
    // Column offset when moving in this direction
    private final int colDelta;

    /**
     * Constructs a Direction with movement deltas.
     * 
     * @param rowDelta change in row when moving this direction
     * @param colDelta change in column when moving this direction
     */
    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    /**
     * Gets the row delta for this direction.
     * @return -1 for UP, 1 for DOWN, 0 for LEFT/RIGHT
     */
    public int getRowDelta() {
        return rowDelta;
    }

    /**
     * Gets the column delta for this direction.
     * @return -1 for LEFT, 1 for RIGHT, 0 for UP/DOWN
     */
    public int getColDelta() {
        return colDelta;
    }

    /**
     * Gets the opposite direction.
     * Used for determining which surfaces swap when rolling.
     * 
     * @return the opposite direction
     */
    public Direction getOpposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return this;
        }
    }

    /**
     * Parses a string to a Direction enum value.
     * Case-insensitive as required by the specification.
     * 
     * @param directionStr the direction string ("up", "down", "left", "right")
     * @return the corresponding Direction, or null if invalid
     */
    public static Direction fromString(String directionStr) {
        if (directionStr == null) return null;
        
        switch (directionStr.toLowerCase()) {
            case "up": return UP;
            case "down": return DOWN;
            case "left": return LEFT;
            case "right": return RIGHT;
            default: return null;
        }
    }
}
