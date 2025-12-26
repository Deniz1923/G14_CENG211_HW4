package game.boxes;

import game.exceptions.EmptyBoxException;
import game.interfaces.Openable;
import game.interfaces.Rollable;
import game.interfaces.Stampable;
import game.tools.SpecialTool;
import game.util.Direction;
import game.util.RandUtil;

/**
 * Abstract base class representing a box in the puzzle game.
 * Implements Rollable, Stampable, and Openable interfaces.
 * Each box has 6 surfaces with letters (A-H), can contain a tool, and tracks
 * open/empty state.
 * <p>
 * Box surfaces are indexed as follows:
 * 0 = Top, 1 = Bottom, 2 = Front, 3 = Back, 4 = Left, 5 = Right
 * <p>
 * Subclasses (RegularBox, UnchangingBox, FixedBox) define specific behaviors
 * for:
 * - Rolling (rotating the box in a direction)
 * - Stamping (changing the top side letter)
 * - Fixing (converting to immovable state)
 */
public abstract class Box implements Rollable, Stampable, Openable {
    // Number of faces on a cube
    public static final int NUM_FACES = 6;

    // Surface index constants for clarity and maintainability
    protected static final int TOP = 0;
    protected static final int BOTTOM = 1;
    protected static final int FRONT = 2;
    protected static final int BACK = 3;
    protected static final int LEFT = 4;
    protected static final int RIGHT = 5;

    // Array storing the letter on each surface
    // Index mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
    private char[] surfaces;

    // The special tool contained in this box (null if empty)
    private SpecialTool tool;

    // Whether the box has been opened by a player
    private boolean isOpen;

    // Whether the box is empty (no tool inside)
    private boolean isEmpty;

    /**
     * Constructs a new Box with randomly generated surface letters.
     * The box starts closed and not empty by default.
     */
    public Box() {
        this.surfaces = RandUtil.generateBoxSurfaces();
        this.tool = null;
        this.isOpen = false;
        this.isEmpty = false;
    }

    /**
     * Gets all 6 surface letters of the box.
     * Returns a copy to prevent external modification.
     *
     * @return array of 6 characters for box surfaces
     */
    public char[] getAllSurfaces() {
        return surfaces.clone();
    }

    /**
     * Sets all 6 surface letters of the box.
     * Used by BoxFixer to preserve surfaces when converting to FixedBox.
     *
     * @param newSurfaces array of exactly 6 characters
     * @throws IllegalArgumentException if array is null or wrong size
     */
    public void setSurfaces(char[] newSurfaces) {
        if (newSurfaces == null || newSurfaces.length != NUM_FACES) {
            throw new IllegalArgumentException("Surfaces array must contain exactly " + NUM_FACES + " characters.");
        }
        this.surfaces = newSurfaces.clone();
    }

    /**
     * Gets the tool contained in this box.
     *
     * @return the tool, or null if empty
     */
    public SpecialTool getTool() {
        return tool;
    }

    /**
     * Sets the tool in this box.
     * Setting to null marks the box as empty.
     *
     * @param tool the tool to set, or null to empty the box
     */
    public void setTool(SpecialTool tool) {
        this.tool = tool;
        this.isEmpty = (tool == null);
    }

    /**
     * Checks if the box is empty (contains no tool).
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Sets the empty status of the box.
     *
     * @param empty true if the box should be marked empty
     */
    public void setEmpty(boolean empty) {
        this.isEmpty = empty;
    }

    /**
     * Checks if the box has been opened by a player.
     *
     * @return true if opened
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Checks if the box has already been opened (Openable interface method).
     *
     * @return true if already opened, false otherwise
     */
    @Override
    public boolean hasBeenOpened() {
        return isOpen;
    }

    /**
     * Sets the open status of the box.
     *
     * @param open true if the box has been opened
     */
    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    /**
     * Opens the box and returns the tool inside (Openable interface method).
     * Marks the box as opened and clears the contained tool.
     *
     * @return The SpecialTool inside the box
     * @throws EmptyBoxException if the box is empty
     */
    @Override
    public SpecialTool open() throws EmptyBoxException {
        if (tool == null) {
            isOpen = true;
            throw new EmptyBoxException("Box is empty!");
        }

        SpecialTool acquiredTool = tool;
        tool = null;
        isOpen = true;
        return acquiredTool;
    }

    /**
     * Gets the letter on the top side of the box.
     * This is the primary letter players try to match to the target.
     *
     * @return the top surface letter (A-H)
     */
    public char getTopSide() {
        return surfaces[0];
    }

    /**
     * Sets the letter on the top side of the box.
     * Used by stamp tools to change the top surface.
     * Note: UnchangingBox and FixedBox override this to prevent changes.
     *
     * @param letter the new top surface letter
     */
    public void setTopSide(char letter) {
        this.surfaces[0] = letter;
    }

    /**
     * Stamps the top side with a new letter (Stampable interface method).
     * Can be overridden by subclasses (UnchangingBox does nothing).
     *
     * @param letter The new letter for the top side
     */
    @Override
    public void stampTopSide(char letter) {
        setTopSide(letter);
    }

    /**
     * Checks if the box can be stamped (Stampable interface method).
     * Must be implemented by subclasses.
     *
     * @return true if the box can be stamped, false otherwise
     */
    @Override
    public abstract boolean canBeStamped();

    /**
     * Flips the box upside down, swapping top and bottom surfaces.
     * Used by the BoxFlipper tool.
     */
    public void flip() {
        char temp = surfaces[TOP];
        surfaces[TOP] = surfaces[BOTTOM];
        surfaces[BOTTOM] = temp;
    }

    /**
     * Returns the type indicator character for grid display.
     * Must be implemented by subclasses.
     *
     * @return 'R' for RegularBox, 'U' for UnchangingBox, 'X' for FixedBox
     */
    public abstract char getTypeChar();

    /**
     * Checks if this box can be rolled.
     * Must be implemented by subclasses.
     *
     * @return true if the box can be rolled
     */
    public abstract boolean canRoll();

    /**
     * Rolls the box in the specified direction using the Direction enum.
     * Rolling rotates 4 surfaces cyclically while 2 remain unchanged.
     * <p>
     * Direction effects (per PDF specification):
     * - RIGHT: Right → Top, Top → Left, Left → Bottom, Bottom → Right
     * - LEFT: Left → Top, Top → Right, Right → Bottom, Bottom → Left
     * - UP: Front → Top, Top → Back, Back → Bottom, Bottom → Front
     * - DOWN: Back → Top, Top → Front, Front → Bottom, Bottom → Back
     *
     * @param direction the Direction enum value
     * @throws IllegalArgumentException if direction is null
     */
    @Override
    public void roll(Direction direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }

        char temp;
        switch (direction) {
            case RIGHT:
                // Rolling right: LEFT→TOP→RIGHT→BOTTOM→LEFT
                temp = surfaces[LEFT];
                surfaces[LEFT] = surfaces[BOTTOM];
                surfaces[BOTTOM] = surfaces[RIGHT];
                surfaces[RIGHT] = surfaces[TOP];
                surfaces[TOP] = temp;
                break;
            case LEFT:
                // Rolling left: RIGHT→TOP→LEFT→BOTTOM→RIGHT
                temp = surfaces[RIGHT];
                surfaces[RIGHT] = surfaces[BOTTOM];
                surfaces[BOTTOM] = surfaces[LEFT];
                surfaces[LEFT] = surfaces[TOP];
                surfaces[TOP] = temp;
                break;
            case UP:
                // Rolling up (forward): FRONT→TOP→BACK→BOTTOM→FRONT
                temp = surfaces[FRONT];
                surfaces[FRONT] = surfaces[BOTTOM];
                surfaces[BOTTOM] = surfaces[BACK];
                surfaces[BACK] = surfaces[TOP];
                surfaces[TOP] = temp;
                break;
            case DOWN:
                // Rolling down (backward): BACK→TOP→FRONT→BOTTOM→BACK
                temp = surfaces[BACK];
                surfaces[BACK] = surfaces[BOTTOM];
                surfaces[BOTTOM] = surfaces[FRONT];
                surfaces[FRONT] = surfaces[TOP];
                surfaces[TOP] = temp;
                break;
        }
    }
}