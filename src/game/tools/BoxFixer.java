package game.tools;

import game.core.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.BoxAlreadyFixedException;

/**
 * A tool that converts a RegularBox or UnchangingBox into a FixedBox.
 * <p>
 * Effect:
 * - The target box is replaced with a new FixedBox
 * - The new FixedBox retains the same surface letters as the original
 * - The new FixedBox is marked as opened
 * - Any tool in the original box is lost
 * <p>
 * Restrictions:
 * - Cannot be used on a box that is already a FixedBox
 * - Throws BoxAlreadyFixedException if attempted
 * <p>
 * Strategy: Useful for "locking in" a box that already has the target letter
 * on top, preventing it from being changed by other actions.
 */
public class BoxFixer extends SpecialTool {

    /**
     * Constructs a new BoxFixer tool.
     */
    public BoxFixer() {
        super("BoxFixer");
    }

    /**
     * Applies the BoxFixer to convert the target box into a FixedBox.
     *
     * @param grid         the box grid
     * @param targetLetter not used by this tool
     * @param row          the row of the target box
     * @param col          the column of the target box
     * @throws BoxAlreadyFixedException if the target is already a FixedBox
     */
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col)
            throws BoxAlreadyFixedException {
        Box targetBox = grid.getBox(row, col);

        // Cannot fix an already fixed box
        if (targetBox instanceof FixedBox) {
            throw new BoxAlreadyFixedException(
                    "Box at R" + (row + 1) + "-C" + (col + 1) + " is already a FixedBox!");
        }

        // Create new FixedBox with same surfaces as the original
        FixedBox fixedBox = new FixedBox();
        fixedBox.setSurfaces(targetBox.getAllSurfaces());
        fixedBox.setOpen(true);

        // Replace the original box with the new FixedBox
        grid.setBox(row, col, fixedBox);
    }
}
