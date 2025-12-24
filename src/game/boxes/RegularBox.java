package game.boxes;

import game.util.RandUtil;

/**
 * A standard box that can be rolled, flipped, and re-stamped.
 * 
 * Probabilities:
 *   - Generation chance: 85% (most common box type)
 *   - Tool chance: 75% (15% for each of 5 tool types, 25% empty)
 * 
 * This is the most versatile box type - it can be freely manipulated by
 * all game mechanics and tools.
 */
public class RegularBox extends Box {
    
    /**
     * Constructs a new RegularBox with random surfaces and a possible tool.
     * 75% chance to contain a tool, 25% chance to be empty.
     */
    public RegularBox() {
        super();
        // Roll for tool: 75% chance to have one
        if (RandUtil.checkChance(75)) {
            // Tool type is randomly selected (20% each)
            this.setTool(RandUtil.generateRandomTool());
        } else {
            // 25% chance: empty box
            this.setTool(null);
            this.setEmpty(true);
        }
    }

    /**
     * Returns the type character for grid display.
     * @return 'R' for RegularBox
     */
    @Override
    public char getTypeChar() {
        return 'R';
    }

    /**
     * Checks if this box can be rolled.
     * RegularBox can always be rolled.
     * @return true
     */
    @Override
    public boolean canRoll() {
        return true;
    }
}
