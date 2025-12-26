package game.core;

import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.UnmovableFixedBoxException;
import game.util.Direction;
import game.util.RandUtil;

import java.util.ArrayList;
import java.util.List;

/*
 * ANSWER TO COLLECTIONS QUESTION:
 *
 * Data Structure Choice: List<List<Box>> (interface) backed by ArrayList (implementation)
 *
 * WHY LIST INTERFACE (NOT ARRAYLIST DIRECTLY)?
 * Declaring the field as List<List<Box>> instead of ArrayList<ArrayList<Box>> follows
 * the Dependency Inversion Principle - we depend on an abstraction (List) rather than
 * a concrete implementation. This decouples our code from ArrayList-specific behavior,
 * allowing future flexibility without breaking existing functionality.
 *
 * WHY ARRAYLIST AS THE BACKING IMPLEMENTATION?
 *
 * Performance Analysis for Grid Operations:
 * +-----------------------+------------+------------+
 * | Operation             | ArrayList  | LinkedList |
 * +-----------------------+------------+------------+
 * | getBox(row, col)      | O(1)       | O(n)       |
 * | setBox(row, col, box) | O(1)       | O(n)       |
 * | Iteration (toString)  | O(n)       | O(n)       |
 * | Memory overhead       | Low        | High       |
 * +-----------------------+------------+------------+
 *
 * Our game performs frequent random access operations (getBox, setBox) during:
 * - Rolling mechanics (accessing boxes along a row/column)
 * - Tool applications (MassRowStamp, MassColumnStamp, PlusShapeStamp)
 * - Grid display (toString iterates all 64 boxes)
 * - Box net viewing (accessing specific box surfaces)
 *
 * ArrayList's contiguous memory layout provides O(1) index-based access and better
 * CPU cache utilization compared to LinkedList's node-based structure.
 *
 * WHY NOT A 2D ARRAY (Box[][])?
 * While arrays offer O(1) access and minimal overhead, List provides:
 * - Type-safe generics (Box[][] requires casting)
 * - Built-in bounds checking with meaningful exceptions
 * - Compatibility with Java Streams and Collections utilities
 * - Cleaner nested iteration syntax with enhanced for-loops
 *
 * CONCLUSION:
 * List<List<Box>> with ArrayList implementation optimally balances OOP design
 * principles (interface-based programming) with runtime efficiency (O(1) access).
 */

/**
 * Represents the 8x8 grid of boxes in the puzzle game.
 * Handles all grid-related operations including rolling, box access, and
 * display.
 * The grid uses 0-based indexing internally but displays 1-based positions to
 * users.
 */
public class BoxGrid {
    // Grid dimensions - fixed 8x8 as per specification
    public static final int GRID_SIZE = 8;

    // The grid is a List of Rows, where each Row is a List of Boxes
    // Using List interface for flexibility, backed by ArrayList for O(1) random
    // access
    private final List<List<Box>> grid;

    /**
     * Constructs a new 8x8 BoxGrid with randomly generated boxes.
     */
    public BoxGrid() {
        this.grid = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the 8x8 grid with randomly generated boxes.
     * Box types are determined by RandUtil.generateRandomBox() with probabilities:
     * - 85% RegularBox
     * - 10% UnchangingBox
     * - 5% FixedBox
     */
    private void initializeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            List<Box> rowList = new ArrayList<>();
            for (int col = 0; col < GRID_SIZE; col++) {
                rowList.add(RandUtil.generateRandomBox());
            }
            grid.add(rowList);
        }
    }

    /**
     * Gets the box at the specified position.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the Box at that position
     */
    public Box getBox(int row, int col) {
        return grid.get(row).get(col);
    }

    /**
     * Sets the box at the specified position.
     * Used by tools like BoxFixer to replace a box with a different type.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @param box the Box to place
     */
    public void setBox(int row, int col, Box box) {
        grid.get(row).set(col, box);
    }

    /**
     * Checks if the given position is on the edge of the grid.
     * Edge positions are: row 0, row 7, column 0, or column 7.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if the position is on the edge
     */
    public boolean isEdge(int row, int col) {
        return row == 0 || row == GRID_SIZE - 1 || col == 0 || col == GRID_SIZE - 1;
    }

    /**
     * Checks if the given position is a corner of the grid.
     * Corners are: (0,0), (0,7), (7,0), (7,7).
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if the position is a corner
     */
    public boolean isCorner(int row, int col) {
        return (row == 0 || row == GRID_SIZE - 1) && (col == 0 || col == GRID_SIZE - 1);
    }

    /**
     * Gets the two valid rolling directions for a corner box.
     * Each corner has exactly two valid inward directions.
     *
     * @param row the row of the corner box
     * @param col the column of the corner box
     * @return array of two valid Direction values, or null if not a corner
     */
    public Direction[] getCornerDirections(int row, int col) {
        // Top-left corner: can go right or down
        if (row == 0 && col == 0)
            return new Direction[] { Direction.RIGHT, Direction.DOWN };
        // Top-right corner: can go left or down
        if (row == 0 && col == GRID_SIZE - 1)
            return new Direction[] { Direction.LEFT, Direction.DOWN };
        // Bottom-left corner: can go right or up
        if (row == GRID_SIZE - 1 && col == 0)
            return new Direction[] { Direction.RIGHT, Direction.UP };
        // Bottom-right corner: can go left or up
        if (row == GRID_SIZE - 1 && col == GRID_SIZE - 1)
            return new Direction[] { Direction.LEFT, Direction.UP };
        return null;
    }

    /**
     * Gets the rolling direction for an edge box (non-corner).
     * Non-corner edge boxes have exactly one valid direction (inward).
     *
     * @param row the row of the edge box
     * @param col the column of the edge box
     * @return the Direction to roll, or null if not an edge
     */
    public Direction getRollDirection(int row, int col) {
        if (row == 0)
            return Direction.DOWN; // Top edge rolls down
        if (row == GRID_SIZE - 1)
            return Direction.UP; // Bottom edge rolls up
        if (col == 0)
            return Direction.RIGHT; // Left edge rolls right
        if (col == GRID_SIZE - 1)
            return Direction.LEFT; // Right edge rolls left
        return null; // Not an edge
    }

    /**
     * Rolls boxes from an edge position with domino effect.
     * All boxes in the row/column roll in sequence until:
     * - A FixedBox is encountered (it and boxes behind it don't roll)
     * - The opposite edge is reached
     *
     * @param startRow        the starting row of the edge box
     * @param startCol        the starting column of the edge box
     * @param chosenDirection the Direction to roll (required for corners, null for
     *                        non-corners)
     * @return true if a FixedBox was encountered during rolling
     * @throws UnmovableFixedBoxException if the starting edge box is a FixedBox
     */
    public boolean rollFromEdge(int startRow, int startCol, Direction chosenDirection)
            throws UnmovableFixedBoxException {
        Box startBox = getBox(startRow, startCol);

        // Check if starting box is a FixedBox - cannot initiate roll from FixedBox
        if (startBox instanceof FixedBox) {
            throw new UnmovableFixedBoxException(
                    "Box at R" + (startRow + 1) + "-C" + (startCol + 1) + " is a FixedBox and CANNOT BE MOVED!");
        }

        // Use chosen direction for corners, otherwise calculate from edge position
        Direction direction = (chosenDirection != null) ? chosenDirection : getRollDirection(startRow, startCol);
        if (direction == null)
            return false;

        // Get row/column delta from the Direction enum
        int dRow = direction.getRowDelta();
        int dCol = direction.getColDelta();

        // Roll boxes in sequence until hitting a FixedBox or grid boundary
        int currentRow = startRow;
        int currentCol = startCol;
        boolean hitFixedBox = false;

        while (currentRow >= 0 && currentRow < GRID_SIZE &&
                currentCol >= 0 && currentCol < GRID_SIZE) {
            Box currentBox = getBox(currentRow, currentCol);

            // FixedBox stops the domino effect - it and boxes behind it don't roll
            if (currentBox instanceof FixedBox) {
                hitFixedBox = true;
                break;
            }

            // Roll the box if it can be rolled (RegularBox and UnchangingBox can)
            if (currentBox.canRoll()) {
                currentBox.roll(direction);
            }

            // Move to the next box in the direction
            currentRow += dRow;
            currentCol += dCol;
        }

        return hitFixedBox;
    }

    /**
     * Counts the number of boxes that have the target letter on top.
     * Used at the end of the game to calculate the player's score.
     *
     * @param targetLetter the target letter to count
     * @return the number of matching boxes
     */
    public int countMatchingBoxes(char targetLetter) {
        int count = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (getBox(row, col).getTopSide() == targetLetter) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if at least one edge box is not a FixedBox.
     * Used to detect if the game should end early due to no possible moves.
     *
     * @return true if any movable edge box exists
     */
    public boolean hasAnyMovableEdgeBox() {
        // Check top and bottom rows
        for (int col = 0; col < GRID_SIZE; col++) {
            if (!(grid.getFirst().get(col) instanceof FixedBox))
                return true;
            if (!(grid.get(GRID_SIZE - 1).get(col) instanceof FixedBox))
                return true;
        }

        // Check left and right columns (excluding corners already checked)
        for (int row = 1; row < GRID_SIZE - 1; row++) {
            if (!(grid.get(row).getFirst() instanceof FixedBox))
                return true;
            if (!(grid.get(row).get(GRID_SIZE - 1) instanceof FixedBox))
                return true;
        }

        return false;
    }

    /**
     * Generates a string showing all 6 sides of a box in a cross-shaped net.
     * <p>
     * Layout (as per specification):
     * -----
     * | C | (Back)
     * -------------
     * | H | B | E | (Left, Top, Right)
     * -------------
     * | F | (Front)
     * -----
     * | A | (Bottom)
     * -----
     *
     * @param row the row of the box
     * @param col the column of the box
     * @return formatted string showing all box sides
     */
    public String getBoxNet(int row, int col) {
        Box box = getBox(row, col);
        char[] surfaces = box.getAllSurfaces();
        // Index Mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right

        return "    -----\n" +
                String.format("    | %c |\n", surfaces[3]) + // Back
                "-------------\n" +
                String.format("| %c | %c | %c |\n", surfaces[4], surfaces[0], surfaces[5]) + // Left, Top, Right
                "-------------\n" +
                String.format("    | %c |\n", surfaces[2]) + // Front
                "    -----\n" +
                String.format("    | %c |\n", surfaces[1]) + // Bottom
                "    -----";
    }

    /**
     * Generates the string representation of the grid for display.
     * Format matches G09/PDF specification:
     * - Column headers: C1 through C8 with proper spacing
     * - Horizontal dashed separator lines between rows
     * - Cell format: | Type-TopLetter-Status |
     * - Type: R (Regular), U (Unchanging), X (Fixed)
     * - TopLetter: The letter on the top side (A-H)
     * - Status: M (Mystery/unopened) or O (Opened/Fixed)
     *
     * @return formatted grid string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Column headers - properly centered above each cell
        sb.append("       C1      C2      C3      C4      C5      C6      C7      C8\n");
        sb.append("--------------------------------------------------------------------------------\n");

        // Grid rows
        for (int row = 0; row < GRID_SIZE; row++) {
            sb.append("R").append(row + 1).append(" | ");

            for (int col = 0; col < GRID_SIZE; col++) {
                Box box = getBox(row, col);
                char type = box.getTypeChar();
                char topSide = box.getTopSide();
                // Status: O if opened or FixedBox, M if mystery (unopened)
                char status = (box.hasBeenOpened() || box instanceof FixedBox) ? 'O' : 'M';
                sb.append(String.format("%c-%c-%c | ", type, topSide, status));
            }

            sb.append("\n");
            sb.append("--------------------------------------------------------------------------------\n");
        }

        return sb.toString();
    }
}
