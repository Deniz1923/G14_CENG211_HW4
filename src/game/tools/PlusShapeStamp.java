package game.tools;

import game.boxes.Box;
import game.core.BoxGrid;

/**
 * A tool that re-stamps the top side of 5 boxes in a plus (+) shape pattern.
 * <p>
 * Effect:
 * - Stamps the selected box and its 4 adjacent neighbors (up, down, left,
 * right)
 * - Each affected box has its top side set to the target letter
 * - Only affects RegularBox (which can be stamped)
 * - Does NOT affect UnchangingBox or FixedBox
 * - Handles grid boundaries gracefully (edge boxes have fewer neighbors)
 * <p>
 * Pattern (X = stamped):
 * X
 * X X X
 * X
 * <p>
 * Strategy: Good for creating clusters of matching boxes.
 * Can affect up to 5 boxes at once if used in the center of the grid.
 */
public class PlusShapeStamp extends SpecialTool {

    /**
     * Constructs a new PlusShapeStamp tool.
     */
    public PlusShapeStamp() {
        super("PlusShapeStamp");
    }

    /**
     * Applies the PlusShapeStamp to stamp boxes in a plus pattern.
     *
     * @param grid         the box grid
     * @param targetLetter the letter to stamp on top of each box
     * @param row          the center row of the plus (0-based)
     * @param col          the center column of the plus (0-based)
     */
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        // Offsets for plus shape: center, up, down, left, right
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] offset : offsets) {
            int targetRow = row + offset[0];
            int targetCol = col + offset[1];

            // Check bounds - skip positions outside the grid
            if (targetRow >= 0 && targetRow < BoxGrid.GRID_SIZE &&
                    targetCol >= 0 && targetCol < BoxGrid.GRID_SIZE) {

                Box box = grid.getBox(targetRow, targetCol);

                // Only stamp boxes that can be stamped (not UnchangingBox or FixedBox)
                if (box.canBeStamped()) {
                    box.stampTopSide(targetLetter);
                }
            }
        }
    }
}
