package game.boxes;

import game.tools.SpecialTool;
import game.util.RandUtil;
public abstract class Box {
    // Index Mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right
    private final char[] surfaces;//index 0 is the top
    private SpecialTool tool;
    private final boolean isOpen;

    public Box(){
        this.surfaces = RandUtil.generateBoxSurfaces();
        this.tool = null;
        this.isOpen = false;
    }

    public abstract boolean canRoll();
    //a -> b -> c -> d -> a
    //4 surfaces are affected
    private void swapSurfaces(int a, int b, int c ,int d){
        char temp = surfaces[a];
        surfaces[a] = surfaces[b];
        surfaces[b] = surfaces[c];
        surfaces[c] = surfaces[d];
        surfaces[d] = temp;
    }

    public void roll(String direction){
        direction = direction.toLowerCase();
        switch(direction){
            case "right":
                swapSurfaces(0,5,1,4);
                break;
            case "left":
                swapSurfaces(0,4,1,5);
                break;
            case "up":
                swapSurfaces(0,3,1,2);
                break;
            case "down":
                swapSurfaces(0,2,1,3);
                break;
            default:
                System.out.println("Given direction is not correct.");
                break;
        }
    }

    public boolean isOpen(){
        return isOpen;
    }

    public char getTopSide(){

        return surfaces[0];
    }
}
