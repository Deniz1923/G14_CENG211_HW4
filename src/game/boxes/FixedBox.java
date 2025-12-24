package game.boxes;

/**
 * A box that cannot be rolled and its top side never changes.
 * Stops the domino effect during rolling (boxes behind it don't roll).
 * Never contains a tool (0% chance, always empty).
 */
public class FixedBox extends Box {

    public FixedBox() {
        super();
        this.setTool(null);
        this.setEmpty(true);
    }

    @Override
    public char getTypeChar() {
        return 'X';
    }

    @Override
    public boolean canRoll() {
        return false;
    }

    @Override
    public void roll(String direction) {
        throw new UnsupportedOperationException("FixedBox cannot be rolled!");
    }

    @Override
    public void setSurfaces(char[] newSurfaces) {
        // FixedBox surfaces can only be set during initialization via BoxFixer
        // After creation, surfaces are immutable
        super.setSurfaces(newSurfaces);
    }

    @Override
    public void setTopSide(char letter) {
        // FixedBox top side cannot be changed by stamp tools
        // Intentionally empty to prevent changes
    }
}
