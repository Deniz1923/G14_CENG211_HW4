package game.boxes;

public class RegularBox extends Box {
    public RegularBox() {
        super();
    }

    @Override
    public boolean canRoll() {
        return true;
    }
}
