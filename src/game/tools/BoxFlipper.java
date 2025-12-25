package game.tools;

import game.core.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.UnmovableFixedBoxException;

/**
 * A tool that flips a box upside down.
 * <p>
 * Effect:
 * - The top side of the box becomes the bottom side
 * - The bottom side of the box becomes the top side
 * - Other surfaces (front, back, left, right) remain unchanged
 * <p>
 * Restrictions:
 * - Cannot be used on a FixedBox (immovable)
 * - Throws UnmovableFixedBoxException if attempted
 * <p>
 * Strategy: Useful when the target letter is on the bottom of a box.
 * Flipping brings it to the top.
 */
public class BoxFlipper extends SpecialTool {

    /**
     * Constructs a new BoxFlipper tool.
     */
    public BoxFlipper() {
        super("BoxFlipper");
    }

    /**
     * Applies the BoxFlipper to flip the target box upside down.
     *
     * @param grid         the box grid
     * @param targetLetter not used by this tool
     * @param row          the row of the target box
     * @param col          the column of the target box
     * @throws UnmovableFixedBoxException if the target is a FixedBox
     */
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col)
            throws UnmovableFixedBoxException {
        Box targetBox = grid.getBox(row, col);

        // FixedBoxes cannot be flipped
        if (targetBox instanceof FixedBox) {
            throw new UnmovableFixedBoxException(
                    "Cannot flip FixedBox at R" + (row + 1) + "-C" + (col + 1) + "!");
        }

        // Flip the box (swaps top and bottom)
        targetBox.flip();
    }
}
