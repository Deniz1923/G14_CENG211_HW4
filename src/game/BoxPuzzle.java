package game;

import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.EmptyBoxException;
import game.exceptions.UnmovableFixedBoxException;
import game.tools.SpecialTool;
import game.util.Direction;
import game.util.RandUtil;

import java.util.Scanner;

/**
 * Main game controller for the Box Top Side Matching Puzzle.
 * Manages the game flow, turn structure, and player interactions.
 * Uses an inner class (GameMenu) for handling menu operations.
 */
public class BoxPuzzle {
    // Total number of turns in the game (as per specification)
    private static final int TOTAL_TURNS = 5;
    
    // The 8x8 grid of boxes
    private final BoxGrid grid;
    
    // The randomly selected target letter (A-H) that players try to match
    private final char targetLetter;
    
    // Inner class handling all menu operations and user interactions
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
        this.menu = new GameMenu();
        this.currentTurn = 1;
        
        startGame();
    }

    /**
     * Starts and runs the main game loop.
     * Each turn consists of two stages:
     *   Stage 1: Edge box selection and rolling (domino effect)
     *   Stage 2: Box opening and tool usage
     */
    private void startGame() {
        menu.displayWelcome();
        menu.displayGrid();
        
        // Main game loop - runs for TOTAL_TURNS turns
        while (currentTurn <= TOTAL_TURNS) {
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
                grid.rollFromEdge(edgeRow, edgeCol, chosenDirection);
                Direction displayDirection = (chosenDirection != null) ? chosenDirection : grid.getRollDirection(edgeRow, edgeCol);
                menu.displayRollSuccess(edgeRow, edgeCol, displayDirection);
                menu.displayGrid();
                
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
                    // Empty box - no tool acquired, but turn continues normally
                    menu.displayEmptyBox(boxRow, boxCol);
                }
                
                // Tool usage is mandatory when a tool is acquired
                if (tool != null) {
                    int[] toolPosition = menu.selectBoxForTool(tool);
                    try {
                        // Apply the tool to the selected box
                        tool.apply(grid, targetLetter, toolPosition[0], toolPosition[1]);
                        menu.displayToolUsed(tool, toolPosition[0], toolPosition[1]);
                    } catch (BoxAlreadyFixedException | UnmovableFixedBoxException e) {
                        // Tool application failed (e.g., BoxFixer on FixedBox)
                        menu.displayError(e.getMessage());
                    }
                }
                
                menu.displayGrid();
                
            } catch (UnmovableFixedBoxException e) {
                // Selected edge box was a FixedBox - turn is wasted
                menu.displayError(e.getMessage());
                menu.displayTurnWasted();
            }
            
            currentTurn++;
        }
        
        // All turns completed - end the game
        endGame();
    }

    /**
     * Opens a box and retrieves its tool using generics.
     * Marks the box as opened and removes the tool from it.
     * 
     * This method demonstrates the use of generics for tool acquisition
     * as required by the specification (T extends SpecialTool).
     *
     * @param <T>  the type of tool, extends SpecialTool
     * @param row  the row of the box (0-based)
     * @param col  the column of the box (0-based)
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
        menu.displayGameEnd();
        menu.close();
    }

    // =========================================================================
    // INNER CLASS: GameMenu
    // =========================================================================

    /**
     * Inner class handling all menu operations and user interactions.
     * Follows the spec requirement to use an inner class for menu handling.
     * Manages all console input/output for the game.
     */
    private class GameMenu {
        // Scanner for reading user input from console
        private final Scanner scanner;

        /**
         * Constructs a new GameMenu with a Scanner for console input.
         */
        public GameMenu() {
            this.scanner = new Scanner(System.in);
        }

        /**
         * Closes the scanner resource.
         * Should be called when the game ends to prevent resource leaks.
         */
        public void close() {
            scanner.close();
        }

        /**
         * Displays the welcome message with game instructions.
         */
        public void displayWelcome() {
            System.out.println();
            System.out.println("Welcome to the Box Top Side Matching Game!");
            System.out.println("The goal of this game is to match the top side of as many boxes as possible");
            System.out.println("to a randomly chosen target letter.");
            System.out.println();
            System.out.println("Target Letter: " + targetLetter);
            System.out.println("You have " + TOTAL_TURNS + " turns.");
            System.out.println();
        }

        /**
         * Displays the header for the first stage of a turn.
         * @param turn the current turn number
         */
        public void displayFirstStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - FIRST STAGE:");
            System.out.println();
        }

        /**
         * Displays the header for the second stage of a turn.
         * @param turn the current turn number
         */
        public void displaySecondStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - SECOND STAGE:");
            System.out.println();
        }

        /**
         * Displays the current state of the grid.
         */
        public void displayGrid() {
            System.out.println(grid.toString());
        }

        /**
         * Prompts user to select an edge box for rolling.
         * Allows viewing a box net before selection (as an optional action).
         * Validates that the selected box is on the edge of the grid.
         *
         * @return [row, col] array of selected edge box (0-based indices)
         */
        public int[] selectEdgeBox() {
            while (true) {
                System.out.println("Enter the edge box you want to roll (format: R1-C5 or 1-5) or VIEW to see a box net:");
                String input = scanner.nextLine().trim().toUpperCase();
                
                // Check if user wants to view a box net
                if (input.equals("VIEW")) {
                    viewBoxNet();
                    continue; // VIEW doesn't consume edge selection - prompt again
                }
                
                // Parse the position input
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# or #-# (e.g., R1-C5 or 1-5)");
                    continue;
                }
                
                int row = position[0];
                int col = position[1];
                
                // Validate that the selected position is on the edge
                if (!grid.isEdge(row, col)) {
                    System.out.println("Selected box is not on the edge! Choose from row 1, row 8, column 1, or column 8.");
                    continue;
                }
                
                return position;
            }
        }

        /**
         * Prompts user to select a direction for a corner box.
         * Corner boxes have two valid rolling directions.
         *
         * @param row the row of the corner box
         * @param col the column of the corner box
         * @return the selected Direction enum value
         */
        public Direction selectCornerDirection(int row, int col) {
            // Get the two valid directions for this corner
            Direction[] directions = grid.getCornerDirections(row, col);
            
            while (true) {
                System.out.println("Corner box selected! Choose direction " + 
                    directions[0].name() + " or " + directions[1].name() + ":");
                String input = scanner.nextLine().trim().toLowerCase();
                
                // Convert input to Direction enum
                Direction selected = Direction.fromString(input);
                
                // Validate the selected direction
                if (selected == directions[0] || selected == directions[1]) {
                    return selected;
                }
                System.out.println("Invalid direction! Enter " + directions[0].name() + 
                    " or " + directions[1].name() + ".");
            }
        }

        /**
         * Displays the net of a user-selected box.
         * Shows all 6 surfaces of the box in a cross-shaped layout.
         */
        private void viewBoxNet() {
            System.out.println("Enter box position to view (format: R1-C5):");
            String input = scanner.nextLine().trim().toUpperCase();
            
            int[] position = parsePosition(input);
            if (position == null) {
                System.out.println("Invalid format!");
                return;
            }
            
            // Display the box net
            System.out.println();
            System.out.println("Box at R" + (position[0] + 1) + "-C" + (position[1] + 1) + ":");
            System.out.println(grid.getBoxNet(position[0], position[1]));
            System.out.println();
        }

        /**
         * Prompts user to select a box to open from the rolled row/column.
         * Only boxes in the same row or column as the roll are valid selections.
         *
         * @param edgeRow the row of the edge box that initiated rolling
         * @param edgeCol the column of the edge box that initiated rolling
         * @param direction the Direction of rolling
         * @return [row, col] of selected box (0-based indices)
         */
        public int[] selectBoxToOpen(int edgeRow, int edgeCol, Direction direction) {
            while (true) {
                System.out.println("Select a box from the rolled row/column to open (format: R1-C5 or 1-5) or VIEW to see a box net:");
                String input = scanner.nextLine().trim().toUpperCase();
                
                // Check if user wants to view a box net
                if (input.equals("VIEW")) {
                    viewBoxNet();
                    continue;
                }
                
                // Parse the position input
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# or #-# (e.g., R1-C5 or 1-5)");
                    continue;
                }
                
                int row = position[0];
                int col = position[1];
                
                // Determine if selection is valid based on roll direction
                boolean validSelection = false;
                
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    // Rolled vertically - selected box must be in same column
                    validSelection = (col == edgeCol);
                } else {
                    // Rolled horizontally - selected box must be in same row
                    validSelection = (row == edgeRow);
                }
                
                if (!validSelection) {
                    System.out.println("Selected box was not in the rolled row/column!");
                    continue;
                }
                
                return position;
            }
        }

        /**
         * Prompts user to select any box for tool application.
         * Tools can be applied to any box on the grid (subject to tool-specific rules).
         *
         * @param tool the tool being applied
         * @return [row, col] of selected box (0-based indices)
         */
        public int[] selectBoxForTool(SpecialTool tool) {
            while (true) {
                System.out.println("Select a box to apply " + tool.getName() + " (format: R1-C5 or 1-5):");
                String input = scanner.nextLine().trim().toUpperCase();
                
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# or #-# (e.g., R1-C5 or 1-5)");
                    continue;
                }
                
                return position;
            }
        }

        /**
         * Displays a success message after rolling boxes.
         */
        public void displayRollSuccess(int row, int col, Direction direction) {
            System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) + 
                    " has been rolled " + direction.name().toLowerCase() + ".");
            System.out.println();
        }

        /**
         * Displays a message when an empty box is opened.
         */
        public void displayEmptyBox(int row, int col) {
            System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) + 
                    " is opened. It is EMPTY! No tool acquired.");
            System.out.println();
        }

        /**
         * Displays the tool acquired from a box.
         */
        public void displayToolAcquired(SpecialTool tool, int row, int col) {
            if (tool != null) {
                System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) + 
                        " is opened. It contains a SpecialTool --> " + tool.getName());
                System.out.println();
            }
        }

        /**
         * Displays a message when a tool is successfully applied.
         */
        public void displayToolUsed(SpecialTool tool, int row, int col) {
            System.out.println(tool.getName() + " applied at R" + (row + 1) + "-C" + (col + 1) + "!");
            System.out.println();
        }

        /**
         * Displays an error message.
         */
        public void displayError(String message) {
            System.out.println("ERROR: " + message);
        }

        /**
         * Displays a message indicating the turn was wasted.
         */
        public void displayTurnWasted() {
            System.out.println("Turn wasted! Continuing to next turn...");
            System.out.println();
        }

        /**
         * Displays the end of game message with final score.
         */
        public void displayGameEnd() {
            System.out.println();
            System.out.println("The final state of the box grid:");
            displayGrid();
            
            // Count how many boxes have the target letter on top
            int matchCount = grid.countMatchingBoxes(targetLetter);
            
            System.out.println("********* GAME OVER *********");
            System.out.println();
            System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + 
                    "\" IN THE BOX GRID --> " + matchCount);
            System.out.println();
            System.out.println("The game has been SUCCESSFULLY completed!");
        }

        /**
         * Parses a position string in format "R#-C#" or "#-#" to [row, col] array.
         * Supports case-insensitive input as required by specification.
         *
         * @param input the position string
         * @return [row, col] as 0-based indices, or null if invalid format
         */
        private int[] parsePosition(String input) {
            try {
                String[] parts = input.split("-");
                if (parts.length != 2) return null;
                
                int row, col;
                
                // Format: R1-C5 or r1-c5 (case-insensitive due to toUpperCase above)
                if (parts[0].startsWith("R") && parts[1].startsWith("C")) {
                    row = Integer.parseInt(parts[0].substring(1)) - 1;
                    col = Integer.parseInt(parts[1].substring(1)) - 1;
                }
                // Format: 1-5 (row-col directly, 1-based)
                else {
                    row = Integer.parseInt(parts[0]) - 1;
                    col = Integer.parseInt(parts[1]) - 1;
                }
                
                // Validate bounds (must be within 0 to GRID_SIZE-1)
                if (row < 0 || row >= BoxGrid.GRID_SIZE || col < 0 || col >= BoxGrid.GRID_SIZE) {
                    return null;
                }
                
                return new int[]{row, col};
            } catch (NumberFormatException e) {
                // Invalid number in input
                return null;
            }
        }
    }
}
