package game.core;

import game.util.RandUtil;

/**
 * Tracks game progress including turn count, target letter, and game status.
 * Manages the overall state of the game across all 5 turns.
 */
public class GameState {
    private final char targetLetter; // Randomly selected target (A-H)
    private int currentTurn; // Current turn number (1-5)
    private static final int MAX_TURNS = 5; // Maximum turns allowed
    private boolean gameOver; // Game termination flag
    private boolean gameSuccess; // Success/failure status

    /**
     * Constructor for GameState.
     * Randomly selects the target letter from A-H.
     */
    public GameState() {
        this.targetLetter = RandUtil.generateTargetLetter();
        this.currentTurn = 1;
        this.gameOver = false;
        this.gameSuccess = false;
    }

    /**
     * Returns the target letter for this game.
     * 
     * @return The target letter (A-H)
     */
    public char getTargetLetter() {
        return targetLetter;
    }

    /**
     * Returns the current turn number.
     * 
     * @return Current turn (1-5)
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Returns the maximum number of turns.
     * 
     * @return The max turns (5)
     */
    public int getMaxTurns() {
        return MAX_TURNS;
    }

    /**
     * Increments the turn counter and checks for game end.
     */
    public void advanceTurn() {
        currentTurn++;
        if (currentTurn > MAX_TURNS) {
            gameOver = true;
            gameSuccess = true; // Completed all 5 turns successfully
        }
    }

    /**
     * Checks if the game has ended.
     * 
     * @return true if game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over status with success/failure flag.
     * 
     * @param success true for successful completion, false for failure
     */
    public void setGameOver(boolean success) {
        this.gameOver = true;
        this.gameSuccess = success;
    }

    /**
     * Checks if the game ended successfully.
     * 
     * @return true if game ended with success
     */
    public boolean isGameSuccess() {
        return gameSuccess;
    }

    /**
     * Checks if there are remaining turns.
     * 
     * @return true if more turns are available
     */
    public boolean hasRemainingTurns() {
        return currentTurn <= MAX_TURNS && !gameOver;
    }
}
