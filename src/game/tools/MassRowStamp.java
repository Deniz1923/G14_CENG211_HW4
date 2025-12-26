package game.tools;

import game.boxes.Box;
import game.core.BoxGrid;

/**
 * A tool that re-stamps the top side of all boxes in a specific row.
 * <p>
 * Effect:
 * - All 8 boxes in the selected row have their top side set to the target
 * letter
 * - Only affects RegularBox (which can be stamped)
 * - Does NOT affect UnchangingBox or FixedBox (their tops remain unchanged)
 * <p>
 * Strategy: Very powerful tool that can set up to 8 boxes at once.
 * Best used on a row with many RegularBoxes.
 */
public class MassRowStamp extends SpecialTool {

    /**
     * Constructs a new MassRowStamp tool.
     */
    public MassRowStamp() {
        super("MassRowStamp");
    }

    /**
     * Applies the MassRowStamp to stamp all boxes in the target row.
     *
     * @param grid         the box grid
     * @param targetLetter the letter to stamp on top of each box
     * @param row          the row to stamp (0-based)
     * @param col          not used - all columns in the row are affected
     */
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        // Iterate through all columns in this row
        for (int c = 0; c < BoxGrid.GRID_SIZE; c++) {
            Box box = grid.getBox(row, c);

            // Only stamp boxes that can be stamped (not UnchangingBox or FixedBox)
            if (box.canBeStamped()) {
                box.stampTopSide(targetLetter);
            }
        }
    }
}
