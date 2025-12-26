package game.core;

import game.boxes.Box;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.EmptyBoxException;
import game.exceptions.InvalidPositionException;
import game.exceptions.UnmovableFixedBoxException;
import game.menu.IMenuDisplay;
import game.menu.IValidator;
import game.menu.InputValidator;
import game.tools.SpecialTool;
import game.util.Direction;
import game.util.RandUtil;

import java.util.Scanner;

/**
 * Main game controller for the Box Top Side Matching Puzzle.
 * Manages the game flow, turn structure, and player interactions.
 * Contains an inner class MenuHandler for all terminal menu operations.
 */
public class BoxPuzzle {
    // Total number of turns in the game (as per specification)
    private static final int TOTAL_TURNS = 5;

    // The 8x8 grid of boxes
    private final BoxGrid grid;

    // The randomly selected target letter (A-H) that players try to match
    private final char targetLetter;

    // Menu handling user interactions and display (inner class instance)
    private final MenuHandler menu;

    // Tracks the current turn number (1 to 5)
    private int currentTurn;

    /**
     * Constructs a new BoxPuzzle game and starts gameplay.
     * Initializes the grid, selects a random target letter, and begins the game
     * loop.
     */
    public BoxPuzzle() {
        this.grid = new BoxGrid();
        this.targetLetter = RandUtil.generateTargetLetter();
        this.menu = new MenuHandler();
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
            // Check if any moves can be made (edge case: all edge boxes are FixedBoxes)
            if (!grid.hasAnyMovableEdgeBox()) {
                menu.displayError("No movable edge boxes remain! Game ending early.");
                break;
            }

            // Display turn header
            menu.displayTurnHeader(currentTurn);

            // Ask if user wants to view a box before starting
            if (menu.askViewSurfaces()) {
                menu.viewBoxNet();
            }

            // FIRST STAGE: Edge box selection and rolling
            menu.displayFirstStageHeader(currentTurn);

            // Store selected edge position and direction for use in catch block
            int edgeRow = -1;
            int edgeCol = -1;
            Direction chosenDirection = null;

            try {
                // Get player's edge box selection
                int[] edgePosition = menu.selectEdgeBox();
                edgeRow = edgePosition[0];
                edgeCol = edgePosition[1];

                // Handle corner case - player must choose direction (corners have 2 valid
                // directions)
                if (grid.isCorner(edgeRow, edgeCol)) {
                    chosenDirection = menu.selectCornerDirection(edgeRow, edgeCol);
                }

                // Execute the roll with domino effect
                boolean hitFixedBox = grid.rollFromEdge(edgeRow, edgeCol, chosenDirection);
                Direction displayDirection = (chosenDirection != null) ? chosenDirection
                        : grid.getRollDirection(edgeRow, edgeCol);
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
                    useTool(tool);
                }

            } catch (UnmovableFixedBoxException e) {
                // Selected edge box was a FixedBox - turn is wasted
                // Use the already captured edgeRow/edgeCol instead of calling selectEdgeBox
                // again
                Direction displayDirection = (chosenDirection != null) ? chosenDirection
                        : grid.getRollDirection(edgeRow, edgeCol);
                menu.displayFixedBoxError(displayDirection);
            }

            currentTurn++;
        }

        // All turns completed - end the game
        endGame();
    }

    /**
     * Applies a special tool to the grid using generics.
     * This method satisfies the requirement for a generic useTool method.
     *
     * @param tool the tool to use, must extend SpecialTool
     * @param <T>  the type of the tool
     */
    private <T extends SpecialTool> void useTool(T tool) {
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

    // =========================================================================
    // INNER CLASS: MenuHandler
    // Handles all terminal menu operations as required by the specification.
    // Implements IMenuDisplay interface for display operations.
    // =========================================================================

    /**
     * Inner class handling all terminal menu operations for the Box Puzzle game.
     * Implements IMenuDisplay for all output operations.
     * Uses IValidator for input validation.
     * Has access to outer class fields (grid, targetLetter) for convenience.
     */
    private class MenuHandler implements IMenuDisplay {

        // Scanner for reading user input from console
        private final Scanner scanner;

        // Validator for parsing and validating user input
        private final IValidator validator;

        /**
         * Constructs a new MenuHandler with a Scanner for console input.
         * Initializes the InputValidator for handling validation logic.
         */
        public MenuHandler() {
            this.scanner = new Scanner(System.in);
            this.validator = new InputValidator();
        }

        /**
         * Closes the scanner resource.
         * Should be called when the game ends to prevent resource leaks.
         */
        public void close() {
            scanner.close();
        }

        // =====================================================================
        // IMenuDisplay Implementation
        // =====================================================================

        /**
         * Displays the welcome message with game instructions.
         * Format matches PDF specification.
         */
        @Override
        public void displayWelcome(char targetLetter) {
            System.out.println();
            System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
            System.out.println(
                    "Your goal is to maximize the letter '" + targetLetter + "' on the top sides of the boxes.");
            System.out.println();
        }

        /**
         * Displays the main turn header.
         */
        @Override
        public void displayTurnHeader(int turn) {
            System.out.println("=====> TURN " + turn + ":");
        }

        /**
         * Displays the header for the first stage of a turn.
         */
        @Override
        public void displayFirstStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - FIRST STAGE:");
            System.out.println();
        }

        /**
         * Displays the header for the second stage of a turn.
         */
        @Override
        public void displaySecondStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - SECOND STAGE:");
            System.out.println();
        }

        /**
         * Displays the current state of the grid.
         */
        @Override
        public void displayGrid(String gridString) {
            System.out.println(gridString);
        }

        /**
         * Displays a success message after rolling boxes.
         * Format matches PDF: "The chosen box and any box on its path have been rolled
         * to the [direction]."
         */
        @Override
        public void displayRollSuccess(Direction direction, boolean hitFixedBox) {
            String directionText = switch (direction) {
                case RIGHT -> "to the right";
                case LEFT -> "to the left";
                case UP -> "upwards";
                case DOWN -> "downwards";
                // Default Not Needed
            };
            // Per PDF: horizontal directions use "to the right/left", vertical use
            // "upwards/downwards"

            System.out.print("The chosen box and any box on its path have been rolled " + directionText);
            if (hitFixedBox) {
                System.out.println(" until a FixedBox has been reached.");
            } else {
                System.out.println(".");
            }
            System.out.println("The new state of the box grid:");
        }

        /**
         * Displays a message when an empty box is opened.
         * Format matches PDF: "BOX IS EMPTY! Continuing to the next turn..."
         */
        @Override
        public void displayEmptyBox() {
            System.out.println("BOX IS EMPTY! Continuing to the next turn...");
            System.out.println();
        }

        /**
         * Displays the tool acquired from a box.
         * Format matches PDF: "The box on location R#-C# is opened. It contains a
         * SpecialTool --> [ToolName]"
         */
        @Override
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
        @Override
        public void displayToolUsed(SpecialTool tool, int row, int col) {
            String toolName = tool.getName();

            switch (toolName) {
                case "BoxFlipper" -> System.out.println("The chosen box on location R" + (row + 1) + "-C" + (col + 1) +
                        " has been flipped upside down.");
                case "PlusShapeStamp" ->
                    System.out.println("Top sides of the chosen box (R" + (row + 1) + "-C" + (col + 1) +
                            ") and its surrounding boxes have been stamped to letter.");
                case "BoxFixer" -> System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) +
                        " has been fixed and cannot be moved.");
                case "MassRowStamp" ->
                    System.out.println("Top sides of all boxes in row " + (row + 1) + " have been stamped.");
                case "MassColumnStamp" ->
                    System.out.println("Top sides of all boxes in column " + (col + 1) + " have been stamped.");
                default -> System.out.println(toolName + " applied at R" + (row + 1) + "-C" + (col + 1) + "!");
            }
            System.out.println("The new state of the box grid:");
        }

        /**
         * Displays an error message.
         */
        @Override
        public void displayError(String message) {
            System.out.println("ERROR: " + message);
        }

        /**
         * Displays the end of game message with final score.
         * Format matches PDF specification.
         */
        @Override
        public void displayGameEnd(String gridString, char targetLetter, int matchCount) {
            System.out.println();
            System.out.println("******** GAME OVER ********");
            System.out.println();
            System.out.println("The final state of the box grid:");
            System.out.println(gridString);
            System.out
                    .println("The total number of \"" + targetLetter + "\" letters on top sides: " + matchCount + ".");
        }

        // =====================================================================
        // Input Methods
        // =====================================================================

        /**
         * Prompts user to select an edge box for rolling.
         * Format matches PDF: "Please enter the location of the edge box you want to
         * roll: "
         *
         * @return [row, col] array of selected edge box (0-based indices)
         */
        public int[] selectEdgeBox() {
            while (true) {
                System.out.print(
                        "Please enter the location of the edge box you want to roll in the format R#-C# or 1-2: ");
                String input = scanner.nextLine();

                // Check if user wants to view a box net
                if (input.trim().equalsIgnoreCase("VIEW")) {
                    viewBoxNet();
                    continue;
                }

                try {
                    // Parse the position input using validator (throws InvalidPositionException if
                    // invalid)
                    int[] position = validator.parsePosition(input);
                    int row = position[0];
                    int col = position[1];

                    // Validate that the selected position is on the edge using validator
                    if (!validator.isEdgePosition(row, col)) {
                        System.out.println(
                                "INCORRECT INPUT: The chosen box is not on any of the edges. Please reenter the location: ");
                        continue;
                    }

                    return position;
                } catch (InvalidPositionException e) {
                    System.out.println("INCORRECT INPUT: " + e.getMessage());
                }
            }
        }

        /**
         * Prompts user to select a direction for a corner box.
         * Format matches PDF for corner direction selection.
         *
         * @param row the row of the corner box
         * @param col the column of the corner box
         * @return the selected Direction enum value
         */
        public Direction selectCornerDirection(int row, int col) {
            // Get the two valid directions for this corner
            Direction[] directions = grid.getCornerDirections(row, col);

            // Build prompt based on corner position (matching PDF format)
            String dir1Text = getDirectionText(directions[0]);
            String dir2Text = getDirectionText(directions[1]);

            while (true) {
                System.out.print("The chosen box can be rolled to either [1] " + dir1Text +
                        " or [2] " + dir2Text + ": ");
                String input = scanner.nextLine().trim();

                // Accept 1 or 2 for selection
                if (input.equals("1")) {
                    return directions[0];
                } else if (input.equals("2")) {
                    return directions[1];
                }

                // Also accept direction name directly
                Direction selected = Direction.fromString(input.toLowerCase());
                if (validator.isValidDirection(selected, directions)) {
                    return selected;
                }

                System.out.println("INCORRECT INPUT: Please enter 1 or 2.");
            }
        }

        /**
         * Gets display text for a direction.
         */
        private String getDirectionText(Direction direction) {
            return switch (direction) {
                case UP -> "upwards";
                case DOWN -> "downwards";
                case LEFT -> "left";
                case RIGHT -> "right";
                // Default Not Needed
            };
        }

        /**
         * Asks user if they want to view all surfaces of a box.
         * Format matches PDF: "Do you want to view all surfaces of a box? [1] Yes or
         * [2] No?"
         *
         * @return true if user wants to view, false otherwise
         */
        public boolean askViewSurfaces() {
            while (true) {
                System.out.print("---> Do you want to view all surfaces of a box? [1] Yes or [2] No? ");
                String input = scanner.nextLine().trim();
                if (input.equals("1"))
                    return true;
                if (input.equals("2"))
                    return false;
                System.out.println("Invalid choice. Please enter 1 or 2:");
            }
        }

        /**
         * Displays the net of a user-selected box.
         * Shows all 6 surfaces of the box in a cross-shaped layout.
         */
        public void viewBoxNet() {
            while (true) {
                System.out.print("Please enter the location of the box you want to view in the format R#-C# or 1-2: ");
                String input = scanner.nextLine();

                try {
                    // Parse position using validator (throws InvalidPositionException if invalid)
                    int[] position = validator.parsePosition(input);

                    // Display the box net
                    System.out.println(grid.getBoxNet(position[0], position[1]));
                    System.out.println();
                    return;
                } catch (InvalidPositionException e) {
                    System.out.println("INCORRECT INPUT: " + e.getMessage());
                }
            }
        }

        /**
         * Prompts user to select a box to open from the rolled row/column.
         * Format matches PDF specification.
         *
         * @param edgeRow   the row of the edge box that initiated rolling
         * @param edgeCol   the column of the edge box that initiated rolling
         * @param direction the Direction of rolling
         * @return [row, col] of selected box (0-based indices)
         */
        public int[] selectBoxToOpen(int edgeRow, int edgeCol, Direction direction) {
            while (true) {
                System.out.print("Please enter the location of the box you want to open: ");
                String input = scanner.nextLine();

                // Check if user wants to view a box net
                if (input.trim().equalsIgnoreCase("VIEW")) {
                    viewBoxNet();
                    continue;
                }

                try {
                    // Parse the position input using validator (throws InvalidPositionException if
                    // invalid)
                    int[] position = validator.parsePosition(input);
                    int row = position[0];
                    int col = position[1];

                    // Validate selection is in rolled row/column using validator
                    if (!validator.isInRolledPath(direction, edgeRow, edgeCol, row, col)) {
                        System.out.println(
                                "INCORRECT INPUT: The chosen box was not rolled during the first stage. Please reenter the location: ");
                        continue;
                    }

                    return position;
                } catch (InvalidPositionException e) {
                    System.out.println("INCORRECT INPUT: " + e.getMessage());
                }
            }
        }

        /**
         * Prompts user to select target for tool application.
         * Uses tool-specific prompts: MassRowStamp asks for row, MassColumnStamp for
         * column,
         * others for position.
         *
         * @param tool the tool being applied
         * @return [row, col] of selected target (0-based indices)
         */
        public int[] selectBoxForTool(SpecialTool tool) {
            String toolName = tool.getName();

            // MassRowStamp: prompt for row only (col is irrelevant)
            if (toolName.equals("MassRowStamp")) {
                while (true) {
                    System.out.print(tool.getUsagePrompt());
                    String input = scanner.nextLine().trim();

                    try {
                        int row = Integer.parseInt(input);
                        if (row >= 1 && row <= 8) {
                            return new int[] { row - 1, 0 }; // Convert to 0-based, col doesn't matter
                        }
                        System.out.println("INCORRECT INPUT: Please enter a row number between 1 and 8.");
                    } catch (NumberFormatException e) {
                        System.out.println("INCORRECT INPUT: Please enter a valid row number (1-8).");
                    }
                }
            }

            // MassColumnStamp: prompt for column only (row is irrelevant)
            if (toolName.equals("MassColumnStamp")) {
                while (true) {
                    System.out.print(tool.getUsagePrompt());
                    String input = scanner.nextLine().trim();

                    try {
                        int col = Integer.parseInt(input);
                        if (col >= 1 && col <= 8) {
                            return new int[] { 0, col - 1 }; // Convert to 0-based, row doesn't matter
                        }
                        System.out.println("INCORRECT INPUT: Please enter a column number between 1 and 8.");
                    } catch (NumberFormatException e) {
                        System.out.println("INCORRECT INPUT: Please enter a valid column number (1-8).");
                    }
                }
            }

            // All other tools: prompt for position (R#-C# format)
            while (true) {
                System.out.print(tool.getUsagePrompt());
                String input = scanner.nextLine();

                try {
                    // Parse position using validator (throws InvalidPositionException if invalid)
                    int[] position = validator.parsePosition(input);
                    return position;
                } catch (InvalidPositionException e) {
                    System.out.println("INCORRECT INPUT: " + e.getMessage());
                }
            }
        }

        /**
         * Displays a message when selecting a FixedBox edge.
         * Format matches PDF specification.
         *
         * @param direction the automatic roll direction
         */
        public void displayFixedBoxError(Direction direction) {
            String directionText = getDirectionText(direction);
            System.out.println("The chosen box is automatically rolled to " + directionText +
                    ". HOWEVER, IT IS FIXED BOX AND CANNOT BE MOVED. Continuing to the next turn...");
            System.out.println();
        }
    }
}
