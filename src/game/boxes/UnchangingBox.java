package game.boxes;

import game.util.RandUtil;

/**
 * A box whose top side letter cannot be changed by stamp tools.
 * <p>
 * Probabilities:
 * - Generation chance: 10%
 * - Tool chance: 100% guaranteed (20% for each of 5 tool types)
 * <p>
 * Behavior:
 * - CAN be rolled (surfaces rotate normally)
 * - CAN be flipped (top and bottom swap)
 * - CANNOT have top side changed by stamp tools (setTopSide has no effect)
 * - CANNOT have surfaces changed by setSurfaces
 * <p>
 * This box type guarantees a tool for the player but resists surface stamping.
 */
public class UnchangingBox extends Box {

    /**
     * Constructs a new UnchangingBox with random surfaces.
     * Always contains a tool (100% chance, 20% for each type).
     */
    public UnchangingBox() {
        super();
        // 100% chance to have a tool (guaranteed)
        this.setTool(RandUtil.generateRandomTool());
    }

    /**
     * Returns the type character for grid display.
     *
     * @return 'U' for UnchangingBox
     */
    @Override
    public char getTypeChar() {
        return 'U';
    }

    /**
     * Checks if this box can be rolled.
     * UnchangingBox can be rolled (surfaces rotate normally).
     *
     * @return true
     */
    @Override
    public boolean canRoll() {
        return true;
    }

    /**
     * Overrides setSurfaces to prevent surface changes.
     * UnchangingBox surfaces cannot be modified after creation.
     *
     * @param newSurfaces ignored - surfaces remain unchanged
     */
    @Override
    public void setSurfaces(char[] newSurfaces) {
        // Intentionally empty - surfaces cannot be changed
    }

    /**
     * Overrides setTopSide to prevent top surface changes.
     * Stamp tools have no effect on UnchangingBox.
     *
     * @param letter ignored - top side remains unchanged
     */
    @Override
    public void setTopSide(char letter) {
        // Intentionally empty - top side cannot be changed by stamps
    }
}