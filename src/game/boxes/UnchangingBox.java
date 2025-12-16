package game.boxes;

public class UnchangingBox extends Box {
    public UnchangingBox() {
        super();
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