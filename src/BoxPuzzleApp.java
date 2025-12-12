import game.BoxPuzzle;

/**
 * The main method of the BoxPuzzleApp class should only initialize a BoxPuzzle object.
 */
public class BoxPuzzleApp {
    public static void main(String[] args) {

        try {
            BoxPuzzle boxPuzzle = new BoxPuzzle();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
