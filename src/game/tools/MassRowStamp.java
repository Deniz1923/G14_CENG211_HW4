package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.boxes.UnchangingBox;

/**
 * A tool that re-stamps the top side of all boxes in the selected box's row.
 * Does not affect UnchangingBox or FixedBox types.
 */
public class MassRowStamp implements SpecialTool {
    
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        for (int c = 0; c < BoxGrid.GRID_SIZE; c++) {
            Box box = grid.getBox(row, c);
            
            // Skip unchangeable boxes
            if (!(box instanceof UnchangingBox) && !(box instanceof FixedBox)) {
                box.setTopSide(targetLetter);
            }
        }
    }
    
    @Override
    public String getName() {
        return "MassRowStamp";
    }
}
