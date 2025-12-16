package game.boxes;

import game.tools.SpecialTool;
import game.util.RandUtil;
import java.util.Arrays;

public abstract class Box {
    public static final int NUM_FACES = 6;
    // Index Mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
    private char[] surfaces; //index 0 is the top
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

    public void setTopSide(char letter) {
        this.surfaces[0] = letter;
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

    public abstract boolean canRoll();

    //4 surfaces are affected, a-> b-> c -> d
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
                swapSurfaces(0, 4, 1, 5);
                break;
            case "left":
                swapSurfaces(0, 5, 1, 4);
                break;
            case "up":
                swapSurfaces(0, 2, 1, 3);
                break;
            case "down":
                swapSurfaces(0, 3, 1, 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }
    }
}