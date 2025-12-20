package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.BoxAlreadyFixedException;

/**
 * A tool that converts a regular or unchanging box into a FixedBox.
 * The new FixedBox retains the same surface letters as the original box.
 */
public class BoxFixer implements SpecialTool {
    
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) 
            throws BoxAlreadyFixedException {
        Box targetBox = grid.getBox(row, col);
        
        if (targetBox instanceof FixedBox) {
            throw new BoxAlreadyFixedException(
                "Box at R" + (row + 1) + "-C" + (col + 1) + " is already a FixedBox!");
        }
        
        // Create new FixedBox with same surfaces
        FixedBox fixedBox = new FixedBox();
        fixedBox.setSurfaces(targetBox.getAllSurfaces());
        fixedBox.setOpen(true);
        
        grid.setBox(row, col, fixedBox);
    }
    
    @Override
    public String getName() {
        return "BoxFixer";
    }
}
