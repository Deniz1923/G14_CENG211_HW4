package game.tools;

import game.BoxGrid;
import game.boxes.Box;
import game.boxes.FixedBox;
import game.boxes.UnchangingBox;

/**
 * A tool that re-stamps the top side of 5 boxes in a plus shape pattern.
 * Affects the selected box and its 4 adjacent neighbors (up, down, left, right).
 * Does not affect UnchangingBox or FixedBox types.
 */
public class PlusShapeStamp implements SpecialTool {
    
    @Override
    public void apply(BoxGrid grid, char targetLetter, int row, int col) {
        // Offsets for plus shape: center, up, down, left, right
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] offset : offsets) {
            int targetRow = row + offset[0];
            int targetCol = col + offset[1];
            
            // Check bounds
            if (targetRow >= 0 && targetRow < BoxGrid.GRID_SIZE &&
                targetCol >= 0 && targetCol < BoxGrid.GRID_SIZE) {
                
                Box box = grid.getBox(targetRow, targetCol);
                
                // Skip unchangeable boxes
                if (!(box instanceof UnchangingBox) && !(box instanceof FixedBox)) {
                    box.setTopSide(targetLetter);
                }
            }
        }
    }
    
    @Override
    public String getName() {
        return "PlusShapeStamp";
    }
}
