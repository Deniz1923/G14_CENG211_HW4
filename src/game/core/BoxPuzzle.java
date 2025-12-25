package game.core;

import game.boxes.Box;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.EmptyBoxException;
import game.exceptions.UnmovableFixedBoxException;
import game.menu.GameMenu;
import game.tools.SpecialTool;
import game.util.Direction;
import game.util.RandUtil;

/**
 * Main game controller for the Box Top Side Matching Puzzle.
 * Manages the game flow, turn structure, and player interactions.
 * Uses GameMenu for handling menu operations.
 */
public class BoxPuzzle {
    // Total number of turns in the game (as per specification)
    private static final int TOTAL_TURNS = 5;

    // The 8x8 grid of boxes
    private final BoxGrid grid;

    // The randomly selected target letter (A-H) that players try to match
    private final char targetLetter;

    // Menu handling user interactions and display
    private final GameMenu menu;

    // Tracks the current turn number (1 to 5)
    private int currentTurn;

    /**
     * Constructs a new BoxPuzzle game and starts gameplay.
     * Initializes the grid, selects a random target letter, and begins the game loop.
     */
    public BoxPuzzle() {
        this.grid = new BoxGrid();
        this.targetLetter = RandUtil.generateTargetLetter();
        this.menu = new GameMenu(grid);
        this.currentTurn = 1;

        startGame();
    }

    /**
     * Starts and runs the main game loop.
     * Each turn consists of two stages:
     * Stage 1: Edge box selection and rolling (domino effect)
     * Stage 2: Box opening and tool usage
     */
    private void startGame() {
        menu.displayWelcome(targetLetter);
        menu.displayGrid(grid.toString());

        // Main game loop - runs for TOTAL_TURNS turns
        while (currentTurn <= TOTAL_TURNS) {
            // Display turn header
            menu.displayTurnHeader(currentTurn);

            // Ask if user wants to view a box before starting
            if (menu.askViewSurfaces()) {
                menu.viewBoxNet();
            }

            // FIRST STAGE: Edge box selection and rolling
            menu.displayFirstStageHeader(currentTurn);

            try {
                // Get player's edge box selection
                int[] edgePosition = menu.selectEdgeBox();

                int edgeRow = edgePosition[0];
                int edgeCol = edgePosition[1];

                // Handle corner case - player must choose direction (corners have 2 valid directions)
                Direction chosenDirection = null;
                if (grid.isCorner(edgeRow, edgeCol)) {
                    chosenDirection = menu.selectCornerDirection(edgeRow, edgeCol);
                }

                // Execute the roll with domino effect
                boolean hitFixedBox = grid.rollFromEdge(edgeRow, edgeCol, chosenDirection);
                Direction displayDirection = (chosenDirection != null) ? chosenDirection : grid.getRollDirection(edgeRow, edgeCol);
                menu.displayRollSuccess(displayDirection, hitFixedBox);
                menu.displayGrid(grid.toString());

                // SECOND STAGE: Box opening for tool
                menu.displaySecondStageHeader(currentTurn);
                int[] boxPosition = menu.selectBoxToOpen(edgeRow, edgeCol, displayDirection);
                int boxRow = boxPosition[0];
                int boxCol = boxPosition[1];

                // Attempt to open the selected box and retrieve a tool
                SpecialTool tool = null;
                try {
                    tool = openBox(boxRow, boxCol);
                    menu.displayToolAcquired(tool, boxRow, boxCol);
                } catch (EmptyBoxException e) {
                    // Empty box - no tool acquired, turn ends
                    menu.displayEmptyBox();
                }

                // Tool usage is mandatory when a tool is acquired
                if (tool != null) {
                    int[] toolPosition = menu.selectBoxForTool(tool);
                    try {
                        // Apply the tool to the selected box
                        tool.apply(grid, targetLetter, toolPosition[0], toolPosition[1]);
                        menu.displayToolUsed(tool, toolPosition[0], toolPosition[1]);
                        menu.displayGrid(grid.toString());
                    } catch (BoxAlreadyFixedException | UnmovableFixedBoxException e) {
                        // Tool application failed (e.g., BoxFixer on FixedBox)
                        menu.displayError(e.getMessage());
                    }
                }

            } catch (UnmovableFixedBoxException e) {
                // Selected edge box was a FixedBox - turn is wasted
                Direction displayDirection = grid.getRollDirection(
                        menu.selectEdgeBox()[0], menu.selectEdgeBox()[1]);
                menu.displayFixedBoxError(displayDirection);
            }

            currentTurn++;
        }

        // All turns completed - end the game
        endGame();
    }

    /**
     * Opens a box and retrieves its tool using generics.
     * Marks the box as opened and removes the tool from it.
     * <p>
     * This method demonstrates the use of generics for tool acquisition
     * as required by the specification (T extends SpecialTool).
     *
     * @param <T> the type of tool, extends SpecialTool
     * @param row the row of the box (0-based)
     * @param col the column of the box (0-based)
     * @return the tool inside the box (cast to type T)
     * @throws EmptyBoxException if the box is empty or already opened
     */
    @SuppressWarnings("unchecked")
    private <T extends SpecialTool> T openBox(int row, int col) throws EmptyBoxException {
        Box box = grid.getBox(row, col);

        // Check if box was already opened in a previous turn
        if (box.isOpen()) {
            throw new EmptyBoxException("Box at R" + (row + 1) + "-C" + (col + 1) + " has already been opened!");
        }

        // Mark the box as opened
        box.setOpen(true);
        SpecialTool tool = box.getTool();

        // Check if box is empty (no tool inside)
        if (tool == null || box.isEmpty()) {
            throw new EmptyBoxException("Box at R" + (row + 1) + "-C" + (col + 1) + " is empty!");
        }

        // Remove tool from box after acquiring (can only be used once)
        box.setTool(null);
        return (T) tool;
    }

    /**
     * Ends the game and displays final results.
     * Shows the final grid state and the count of matching boxes.
     */
    private void endGame() {
        int matchCount = grid.countMatchingBoxes(targetLetter);
        menu.displayGameEnd(grid.toString(), targetLetter, matchCount);
        menu.close();
    }
}
