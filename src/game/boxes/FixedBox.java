package game.boxes;

public class FixedBox extends Box{

    public FixedBox(){
        super();
        this.setTool(null);
    }
    @Override
    public boolean canRoll() {
        return false;
    }

    @Override
    public void roll(String direction){
        throw new UnsupportedOperationException();
    }
}
