package game.tools;

import game.BoxGrid;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.UnmovableFixedBoxException;

/**
 * Interface for special tools that can be found inside boxes.
 * Each tool has a unique effect on the box grid when applied.
 */
public interface SpecialTool {
    
    /**
     * Applies the tool's effect to the grid at the specified position.
     *
     * @param grid         the box grid to modify
     * @param targetLetter the target letter for stamp tools
     * @param row          the row of the selected box
     * @param col          the column of the selected box
     * @throws BoxAlreadyFixedException    if BoxFixer is used on a FixedBox
     * @throws UnmovableFixedBoxException  if BoxFlipper is used on a FixedBox
     */
    void apply(BoxGrid grid, char targetLetter, int row, int col) 
            throws BoxAlreadyFixedException, UnmovableFixedBoxException;
    
    /**
     * Returns the name of the tool for display purposes.
     *
     * @return the tool name
     */
    String getName();
}
