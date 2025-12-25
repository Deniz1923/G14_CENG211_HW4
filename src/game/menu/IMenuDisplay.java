package game.menu;

import game.tools.SpecialTool;
import game.util.Direction;

/**
 * Interface for menu display operations in the Box Puzzle game.
 * Separates display/output responsibilities from input and game logic.
 * Implementations handle all console output operations.
 */
public interface IMenuDisplay {

    /**
     * Displays the welcome message with game instructions.
     *
     * @param targetLetter the randomly chosen target letter
     */
    void displayWelcome(char targetLetter);

    /**
     * Displays the main turn header.
     *
     * @param turn the current turn number
     */
    void displayTurnHeader(int turn);

    /**
     * Displays the header for the first stage of a turn.
     *
     * @param turn the current turn number
     */
    void displayFirstStageHeader(int turn);

    /**
     * Displays the header for the second stage of a turn.
     *
     * @param turn the current turn number
     */
    void displaySecondStageHeader(int turn);

    /**
     * Displays the current state of the grid.
     *
     * @param gridString the string representation of the grid
     */
    void displayGrid(String gridString);

    /**
     * Displays a success message after rolling boxes.
     *
     * @param direction the direction of rolling
     * @param hitFixedBox true if a FixedBox blocked the roll
     */
    void displayRollSuccess(Direction direction, boolean hitFixedBox);

    /**
     * Displays a message when an empty box is opened.
     */
    void displayEmptyBox();

    /**
     * Displays the tool acquired from a box.
     *
     * @param tool the tool acquired
     * @param row  the row of the box
     * @param col  the column of the box
     */
    void displayToolAcquired(SpecialTool tool, int row, int col);

    /**
     * Displays a message when a tool is successfully applied.
     *
     * @param tool the tool applied
     * @param row  the row of the target box
     * @param col  the column of the target box
     */
    void displayToolUsed(SpecialTool tool, int row, int col);

    /**
     * Displays an error message.
     *
     * @param message the error message to display
     */
    void displayError(String message);

    /**
     * Displays the end of game message with final score.
     *
     * @param gridString   the final grid state
     * @param targetLetter the target letter
     * @param matchCount   the count of matching boxes
     */
    void displayGameEnd(String gridString, char targetLetter, int matchCount);
}
