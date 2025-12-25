import game.core.BoxPuzzle;

/**
 * BoxPuzzleApp - Main entry point for the Box Top Side Matching Puzzle Game.
 * <p>
 * This application implements a puzzle game where players attempt to match
 * the top sides of boxes on an 8x8 grid to a randomly selected target letter.
 * <p>
 * The game consists of 5 turns, where each turn has two stages:
 * 1. First Stage: Select an edge box to roll (creates domino effect)
 * 2. Second Stage: Open a box and use any tool found inside
 */
public class BoxPuzzleApp {

    /**
     * Main method - Entry point of the application.
     * Creates a new BoxPuzzle instance which automatically starts the game.
     * All exceptions are caught and displayed to prevent abnormal termination.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Wrap game initialization in try-catch to ensure the application
        // never terminates abnormally as required by the specification
        try {
            // Create and start a new game instance
            BoxPuzzle boxPuzzle = new BoxPuzzle();
        } catch (Exception e) {
            // Display any unexpected errors to the user
            System.out.println(e.getMessage());
        }
    }
}
