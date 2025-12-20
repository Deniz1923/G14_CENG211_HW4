package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.boxes.UnchangingBox;

/**
 * A tool that re-stamps the top side of all boxes in the selected box's column.
 * Does not affect UnchangingBox or FixedBox types.
 */
public class MassColumnStamp implements SpecialTool {
    
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        for (int r = 0; r < BoxGrid.GRID_SIZE; r++) {
            Box box = grid.getBox(r, col);
            
            // Skip unchangeable boxes
            if (!(box instanceof UnchangingBox) && !(box instanceof FixedBox)) {
                box.setTopSide(targetLetter);
            }
        }
    }
    
    @Override
    public String getName() {
        return "MassColumnStamp";
    }
}
