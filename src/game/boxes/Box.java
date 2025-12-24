package game.boxes;

import game.tools.SpecialTool;
import game.util.RandUtil;

/**
 * Abstract base class representing a box in the puzzle game.
 * Each box has 6 surfaces with letters, can contain a tool, and tracks open/empty state.
 * Subclasses define specific behaviors for rolling, stamping, and fixing.
 */
public abstract class Box {
    public static final int NUM_FACES = 6;
    
    // Surface index constants for clarity
    protected static final int TOP = 0;
    protected static final int BOTTOM = 1;
    protected static final int FRONT = 2;
    protected static final int BACK = 3;
    protected static final int LEFT = 4;
    protected static final int RIGHT = 5;
    
    private char[] surfaces; // Index mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
    private SpecialTool tool;
    private boolean isOpen;
    private boolean isEmpty;

    public Box() {
        this.surfaces = RandUtil.generateBoxSurfaces();
        this.tool = null;
        this.isOpen = false;
        this.isEmpty = false;
    }

    public char[] getAllSurfaces() {
        return surfaces.clone();
    }

    public void setSurfaces(char[] newSurfaces) {
        if (newSurfaces == null || newSurfaces.length != NUM_FACES) {
            throw new IllegalArgumentException("Surfaces array must contain exactly " + NUM_FACES + " characters.");
        }
        this.surfaces = newSurfaces.clone();
    }

    public SpecialTool getTool() {
        return tool;
    }

    public void setTool(SpecialTool tool) {
        this.tool = tool;
        this.isEmpty = (tool == null);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        this.isEmpty = empty;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public char getTopSide() {
        return surfaces[0];
    }

    public void setTopSide(char letter) {
        this.surfaces[0] = letter;
    }

    /**
     * Flips the box upside down, swapping top and bottom surfaces.
     */
    public void flip() {
        char temp = surfaces[TOP];
        surfaces[TOP] = surfaces[BOTTOM];
        surfaces[BOTTOM] = temp;
    }

    /**
     * Returns the type indicator character for grid display.
     * R = RegularBox, U = UnchangingBox, X = FixedBox
     *
     * @return the type character
     */
    public abstract char getTypeChar();

    public abstract boolean canRoll();

    /**
     * Rotates 4 surfaces in a cycle: a -> b -> c -> d -> a
     */
    private void swapSurfaces(int a, int b, int c, int d) {
        char temp = surfaces[a];
        surfaces[a] = surfaces[b];
        surfaces[b] = surfaces[c];
        surfaces[c] = surfaces[d];
        surfaces[d] = temp;
    }

    public void roll(String direction) {
        if (direction == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }

        switch (direction.toLowerCase()) {
            case "right":
                swapSurfaces(TOP, LEFT, BOTTOM, RIGHT);
                break;
            case "left":
                swapSurfaces(TOP, RIGHT, BOTTOM, LEFT);
                break;
            case "up":
                swapSurfaces(TOP, FRONT, BOTTOM, BACK);
                break;
            case "down":
                swapSurfaces(TOP, BACK, BOTTOM, FRONT);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}