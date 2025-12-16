package game.exceptions;

public class BoxAlreadyFixedException extends RuntimeException {
    public BoxAlreadyFixedException() {
        super("This box is already fixed!");
    }
}
