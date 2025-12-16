package game.exceptions;

public class UnmovableFixedBoxException extends RuntimeException {
    public UnmovableFixedBoxException() {
        super("Fixed box cannot be moved!");
    }
}
