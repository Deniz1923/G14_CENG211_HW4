package game.boxes;

import game.util.RandUtil;

/**
 * A box whose surfaces cannot be changed by stamp tools.
 * Can be rolled and flipped, but setTopSide and setSurfaces have no effect.
 * Always contains a tool (100% chance, 20% for each tool type).
 */
public class UnchangingBox extends Box {
    
    public UnchangingBox() {
        super();
        // 100% chance to have a tool
        this.setTool(RandUtil.generateRandomTool());
    }

    @Override
    public char getTypeChar() {
        return 'U';
    }

    @Override
    public boolean canRoll() {
        return true;
    }

    @Override
    public void setSurfaces(char[] newSurfaces) {
        // Intentionally empty to prevent changes
    }

    @Override
    public void setTopSide(char letter) {
        // Intentionally empty to prevent changes
    }
}