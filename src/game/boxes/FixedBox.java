package game.boxes;

/**
 * A box that cannot be rolled, flipped, or have its surfaces changed.
 * <p>
 * Probabilities:
 * - Generation chance: 5% (rarest box type)
 * - Tool chance: 0% (always empty)
 * <p>
 * Behavior:
 * - CANNOT be rolled (throws UnsupportedOperationException if attempted)
 * - CANNOT be flipped (BoxFlipper will throw exception)
 * - CANNOT have surfaces changed
 * - STOPS the domino effect during rolling (boxes behind it don't roll)
 * - Always displayed as "opened" (status 'O') because it has no mystery
 * <p>
 * FixedBoxes can be created by:
 * 1. Random generation (5% chance during grid initialization)
 * 2. Using the BoxFixer tool on a RegularBox or UnchangingBox
 */
public class FixedBox extends Box {

    /**
     * Constructs a new FixedBox with random surfaces.
     * Always empty (no tool inside).
     */
    public FixedBox() {
        super();
        this.setTool(null);   // No tool
        this.setEmpty(true);  // Always empty
    }

    /**
     * Returns the type character for grid display.
     *
     * @return 'X' for FixedBox
     */
    @Override
    public char getTypeChar() {
        return 'X';
    }

    /**
     * Checks if this box can be rolled.
     * FixedBox cannot be rolled - it's immovable.
     *
     * @return false
     */
    @Override
    public boolean canRoll() {
        return false;
    }

    /**
     * Attempting to roll a FixedBox throws an exception.
     * This should not be called due to canRoll() returning false,
     * but provides a safety net.
     *
     * @param direction ignored
     * @throws UnsupportedOperationException always
     */
    @Override
    public void roll(game.util.Direction direction) {
        throw new UnsupportedOperationException("FixedBox cannot be rolled!");
    }

    /**
     * Sets the surfaces of this FixedBox.
     * This is only used during creation by BoxFixer tool to copy
     * surfaces from the original box. After that, surfaces are fixed.
     *
     * @param newSurfaces the surfaces to set
     */
    @Override
    public void setSurfaces(char[] newSurfaces) {
        // Allow setting during initialization (used by BoxFixer)
        super.setSurfaces(newSurfaces);
    }

    /**
     * Overrides setTopSide to prevent top surface changes.
     * FixedBox surfaces are immutable after creation.
     *
     * @param letter ignored - top side remains unchanged
     */
    @Override
    public void setTopSide(char letter) {
        // Intentionally empty - FixedBox top side cannot be changed
    }
}
