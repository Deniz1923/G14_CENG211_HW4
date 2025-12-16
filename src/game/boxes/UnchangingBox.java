package game.boxes;

public class UnchangingBox extends Box{
    @Override
    public boolean canRoll() {
        return false;
    }
}
