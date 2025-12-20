package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.UnmovableFixedBoxException;

/**
 * A tool that flips a box upside down.
 * The top side becomes the bottom and vice versa.
 */
public class BoxFlipper implements SpecialTool {
    
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) 
            throws UnmovableFixedBoxException {
        Box targetBox = grid.getBox(row, col);
        
        if (targetBox instanceof FixedBox) {
            throw new UnmovableFixedBoxException(
                "Cannot flip FixedBox at R" + (row + 1) + "-C" + (col + 1) + "!");
        }
        
        targetBox.flip();
    }
    
    @Override
    public String getName() {
        return "BoxFlipper";
    }
}
