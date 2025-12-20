package game;

import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.UnmovableFixedBoxException;
import game.util.RandUtil;

import java.util.ArrayList;

/**
 * ANSWER TO COLLECTIONS QUESTION:
 * We chose ArrayList<ArrayList<Box>> for the following reasons:
 * 1. Random Access: ArrayList provides O(1) access by index, which is essential for
 *    accessing boxes at specific grid positions (row, column) during gameplay.
 * 2. Dynamic Sizing: While the grid is fixed at 8x8, ArrayList handles initialization
 *    cleanly without needing to specify size at compile time.
 * 3. Flexibility: ArrayList allows easy iteration and modification of elements,
 *    which is useful for rolling operations and tool applications.
 * 4. Memory Efficiency: For a fixed-size grid, ArrayList has minimal overhead and
 *    good cache locality for row-wise access patterns.
 * Alternative considered: LinkedList would have O(n) access time which is suboptimal
 * for frequent grid access operations in this game.
 */
public class BoxGrid {
    public static final int GRID_SIZE = 8;
    
    // The grid is a List of Rows, where each Row is a List of Boxes
    private final ArrayList<ArrayList<Box>> grid;

    public BoxGrid() {
        this.grid = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Initializes the 8x8 grid with randomly generated boxes.
     * Box types are determined by RandUtil.generateRandomBox() with probabilities:
     * 85% RegularBox, 10% UnchangingBox, 5% FixedBox
     */
    private void initializeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            ArrayList<Box> rowList = new ArrayList<>();
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
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if the position is on the edge
     */
    public boolean isEdge(int row, int col) {
        return row == 0 || row == GRID_SIZE - 1 || col == 0 || col == GRID_SIZE - 1;
    }

    /**
     * Gets the rolling direction for an edge box.
     *
     * @param row the row of the edge box
     * @param col the column of the edge box
     * @return the direction to roll ("up", "down", "left", "right")
     */
    public String getRollDirection(int row, int col) {
        if (row == 0) return "down";
        if (row == GRID_SIZE - 1) return "up";
        if (col == 0) return "right";
        if (col == GRID_SIZE - 1) return "left";
        return null; // Not an edge
    }

    /**
     * Rolls boxes from an edge position with domino effect.
     * FixedBoxes stop the domino effect (they and boxes behind them don't roll).
     *
     * @param startRow the starting row of the edge box
     * @param startCol the starting column of the edge box
     * @throws UnmovableFixedBoxException if the starting edge box is a FixedBox
     */
    public void rollFromEdge(int startRow, int startCol) throws UnmovableFixedBoxException {
        Box startBox = getBox(startRow, startCol);
        
        // Check if starting box is a FixedBox
        if (startBox instanceof FixedBox) {
            throw new UnmovableFixedBoxException(
                "Box at R" + (startRow + 1) + "-C" + (startCol + 1) + " is a FixedBox and CANNOT BE MOVED!");
        }
        
        String direction = getRollDirection(startRow, startCol);
        if (direction == null) return;
        
        // Determine the row/column delta based on direction
        int dRow = 0, dCol = 0;
        switch (direction) {
            case "up": dRow = -1; break;
            case "down": dRow = 1; break;
            case "left": dCol = -1; break;
            case "right": dCol = 1; break;
        }
        
        // Roll boxes in sequence until hitting a FixedBox or edge
        int currentRow = startRow;
        int currentCol = startCol;
        
        while (currentRow >= 0 && currentRow < GRID_SIZE && 
               currentCol >= 0 && currentCol < GRID_SIZE) {
            Box currentBox = getBox(currentRow, currentCol);
            
            if (currentBox instanceof FixedBox) {
                // Stop rolling - FixedBox blocks further propagation
                break;
            }
            
            if (currentBox.canRoll()) {
                currentBox.roll(direction);
            }
            
            currentRow += dRow;
            currentCol += dCol;
        }
    }

    /**
     * Counts the number of boxes that have the target letter on top.
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
     * Displays the full net (all 6 sides) of a box.
     * Format:
     *     -----
     *     | C |    (Back)
     * -------------
     * | H | B | E |  (Left, Top, Right)
     * -------------
     *     | F |    (Front)
     *     -----
     *     | A |    (Bottom)
     *     -----
     *
     * @param row the row of the box
     * @param col the column of the box
     * @return formatted string showing all box sides
     */
    public String getBoxNet(int row, int col) {
        Box box = getBox(row, col);
        char[] surfaces = box.getAllSurfaces();
        // Index Mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
        
        StringBuilder sb = new StringBuilder();
        sb.append("    -----\n");
        sb.append(String.format("    | %c |\n", surfaces[3])); // Back
        sb.append("-------------\n");
        sb.append(String.format("| %c | %c | %c |\n", surfaces[4], surfaces[0], surfaces[5])); // Left, Top, Right
        sb.append("-------------\n");
        sb.append(String.format("    | %c |\n", surfaces[2])); // Front
        sb.append("    -----\n");
        sb.append(String.format("    | %c |\n", surfaces[1])); // Bottom
        sb.append("    -----");
        return sb.toString();
    }

    /**
     * Generates the string representation of the grid.
     * Format: | Type-TopLetter-Status | where Status is M (Mystery) or O (Opened/Fixed)
     *
     * @return formatted grid string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Header row with column numbers
        sb.append("        ");
        for (int col = 0; col < GRID_SIZE; col++) {
            sb.append(String.format("C%d      ", col + 1));
        }
        sb.append("\n");
        
        // Grid rows
        for (int row = 0; row < GRID_SIZE; row++) {
            sb.append(String.format("R%d  ", row + 1));
            for (int col = 0; col < GRID_SIZE; col++) {
                Box box = getBox(row, col);
                char type = box.getTypeChar();
                char topSide = box.getTopSide();
                char status = (box.isOpen() || box instanceof FixedBox) ? 'O' : 'M';
                sb.append(String.format("| %c-%c-%c |", type, topSide, status));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
