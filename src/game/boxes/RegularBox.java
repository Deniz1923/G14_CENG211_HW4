package game.boxes;

import game.util.RandUtil;

/**
 * A standard box that can be rolled, flipped, and re-stamped.
 * Has 75% chance to contain a tool (15% for each of 5 tools).
 */
public class RegularBox extends Box {
    
    public RegularBox() {
        super();
        // 75% chance to have a tool (25% empty)
        if (RandUtil.checkChance(75)) {
            this.setTool(RandUtil.generateRandomTool());
        } else {
            this.setTool(null);
            this.setEmpty(true);
        }
    }

    @Override
    public char getTypeChar() {
        return 'R';
    }

    @Override
    public boolean canRoll() {
        return true;
    }
}
