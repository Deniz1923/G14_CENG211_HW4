package game.interfaces;

import game.util.Direction;

/**
 * Interface for objects that can be rolled in a direction.
 * Implemented by Box and its subclasses.
 */
public interface Rollable {

    /**
     * Rolls the object in the specified direction.
     *
     * @param direction The direction to roll
     */
    void roll(Direction direction);

    /**
     * Checks if the object can be rolled.
     *
     * @return true if the object can be rolled, false otherwise (e.g., FixedBox
     * returns false)
     */
    boolean canRoll();
}
