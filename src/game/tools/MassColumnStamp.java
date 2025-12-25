package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.boxes.UnchangingBox;

/**
 * A tool that re-stamps the top side of all boxes in a specific column.
 * <p>
 * Effect:
 * - All 8 boxes in the selected column have their top side set to the target letter
 * - Only affects RegularBox (which can be stamped)
 * - Does NOT affect UnchangingBox or FixedBox (their tops remain unchanged)
 * <p>
 * Strategy: Very powerful tool that can set up to 8 boxes at once.
 * Best used on a column with many RegularBoxes.
 */
public class MassColumnStamp extends SpecialTool {

    /**
     * Constructs a new MassColumnStamp tool.
     */
    public MassColumnStamp() {
        super("MassColumnStamp");
    }

    /**
     * Applies the MassColumnStamp to stamp all boxes in the target column.
     *
     * @param grid         the box grid
     * @param targetLetter the letter to stamp on top of each box
     * @param row          not used - all rows in the column are affected
     * @param col          the column to stamp (0-based)
     */
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        // Iterate through all rows in this column
        for (int r = 0; r < BoxGrid.GRID_SIZE; r++) {
            Box box = grid.getBox(r, col);

            // Skip UnchangingBox and FixedBox - their surfaces can't be changed
            if (!(box instanceof UnchangingBox) && !(box instanceof FixedBox)) {
                box.setTopSide(targetLetter);
            }
        }
    }
}
