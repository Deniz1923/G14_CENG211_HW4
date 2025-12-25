package game.menu;

import game.core.BoxGrid;
import game.tools.SpecialTool;
import game.util.Direction;

import java.util.Scanner;

/**
 * Menu class handling all user interactions and display for the Box Puzzle game.
 * Implements IMenuDisplay for all output operations.
 * Uses IValidator for input validation.
 * Output format matches the PDF specification.
 */
public class GameMenu implements IMenuDisplay {

    // Scanner for reading user input from console
    private final Scanner scanner;

    // Validator for parsing and validating user input
    private final IValidator validator;

    // Reference to the grid for display and validation
    private final BoxGrid grid;

    /**
     * Constructs a new GameMenu with a Scanner for console input.
     * Initializes the InputValidator for handling validation logic.
     *
     * @param grid the game grid for display purposes
     */
    public GameMenu(BoxGrid grid) {
        this.scanner = new Scanner(System.in);
        this.validator = new InputValidator();
        this.grid = grid;
    }

    /**
     * Closes the scanner resource.
     * Should be called when the game ends to prevent resource leaks.
     */
    public void close() {
        scanner.close();
    }

    // =========================================================================
    // IMenuDisplay Implementation
    // =========================================================================

    /**
     * Displays the welcome message with game instructions.
     * Format matches PDF specification.
     */
    @Override
    public void displayWelcome(char targetLetter) {
        System.out.println();
        System.out.println("Welcome to Box Top Side Matching Puzzle App. An 8x8 box grid is being generated.");
        System.out.println("Your goal is to maximize the letter \"" + targetLetter + "\" on the top sides of the boxes.");
        System.out.println();
    }

    /**
     * Displays the main turn header.
     */
    @Override
    public void displayTurnHeader(int turn) {
        System.out.println("------> TURN " + turn + ":");
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
     * Format matches PDF: "The chosen box and any box on its path have been rolled to the [direction]."
     */
    @Override
    public void displayRollSuccess(Direction direction, boolean hitFixedBox) {
        String directionText;
        switch (direction) {
            case UP:
                directionText = "upwards";
                break;
            case DOWN:
                directionText = "downwards";
                break;
            case LEFT:
                directionText = "left";
                break;
            case RIGHT:
                directionText = "right";
                break;
            default:
                directionText = direction.name().toLowerCase();
        }

        System.out.print("The chosen box and any box on its path have been rolled to the " + directionText);
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
     * Format matches PDF: "The box on location R#-C# is opened. It contains a SpecialTool --> [ToolName]"
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

        if (toolName.equals("BoxFlipper")) {
            System.out.println("The chosen box on location R" + (row + 1) + "-C" + (col + 1) +
                    " has been flipped upside down.");
        } else if (toolName.equals("PlusShapeStamp")) {
            System.out.println("Top sides of the chosen box (R" + (row + 1) + "-C" + (col + 1) +
                    ") and its surrounding boxes have been stamped to letter.");
        } else if (toolName.equals("BoxFixer")) {
            System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) +
                    " has been fixed and cannot be moved.");
        } else if (toolName.equals("MassRowStamp")) {
            System.out.println("Top sides of all boxes in row " + (row + 1) + " have been stamped.");
        } else if (toolName.equals("MassColumnStamp")) {
            System.out.println("Top sides of all boxes in column " + (col + 1) + " have been stamped.");
        } else {
            System.out.println(toolName + " applied at R" + (row + 1) + "-C" + (col + 1) + "!");
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
        System.out.println("The total number of \"" + targetLetter + "\" letters on top sides: " + matchCount + ".");
    }

    // =========================================================================
    // Input Methods (keep Scanner operations here)
    // =========================================================================

    /**
     * Prompts user to select an edge box for rolling.
     * Format matches PDF: "Please enter the location of the edge box you want to roll: "
     *
     * @return [row, col] array of selected edge box (0-based indices)
     */
    public int[] selectEdgeBox() {
        while (true) {
            System.out.print("Please enter the location of the edge box you want to roll: ");
            String input = scanner.nextLine();

            // Check if user wants to view a box net
            if (input.trim().equalsIgnoreCase("VIEW")) {
                viewBoxNet();
                continue;
            }

            // Parse the position input using validator
            int[] position = validator.parsePosition(input);
            if (position == null) {
                System.out.println("INCORRECT INPUT: Invalid format. Please reenter the location: ");
                continue;
            }

            int row = position[0];
            int col = position[1];

            // Validate that the selected position is on the edge using validator
            if (!validator.isEdgePosition(row, col)) {
                System.out.println("INCORRECT INPUT: The chosen box is not on any of the edges. Please reenter the location: ");
                continue;
            }

            return position;
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
        switch (direction) {
            case UP:
                return "upwards";
            case DOWN:
                return "downwards";
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
            default:
                return direction.name().toLowerCase();
        }
    }

    /**
     * Asks user if they want to view all surfaces of a box.
     * Format matches PDF: "Do you want to view all surfaces of a box? [1] Yes or [2] No?"
     *
     * @return true if user wants to view, false otherwise
     */
    public boolean askViewSurfaces() {
        System.out.print("---> Do you want to view all surfaces of a box? [1] Yes or [2] No? ");
        String input = scanner.nextLine().trim();
        return input.equals("1") || input.equalsIgnoreCase("yes");
    }

    /**
     * Displays the net of a user-selected box.
     * Shows all 6 surfaces of the box in a cross-shaped layout.
     */
    public void viewBoxNet() {
        System.out.print("Please enter the location of the box you want to view: ");
        String input = scanner.nextLine();

        // Parse position using validator
        int[] position = validator.parsePosition(input);
        if (position == null) {
            System.out.println("INCORRECT INPUT: Invalid format!");
            return;
        }

        // Display the box net
        System.out.println(grid.getBoxNet(position[0], position[1]));
        System.out.println();
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

            // Parse the position input using validator
            int[] position = validator.parsePosition(input);
            if (position == null) {
                System.out.println("INCORRECT INPUT: Invalid format. Please reenter the location: ");
                continue;
            }

            int row = position[0];
            int col = position[1];

            // Validate selection is in rolled row/column using validator
            if (!validator.isInRolledPath(direction, edgeRow, edgeCol, row, col)) {
                System.out.println("INCORRECT INPUT: The chosen box was not rolled during the first stage. Please reenter the location: ");
                continue;
            }

            return position;
        }
    }

    /**
     * Prompts user to select any box for tool application.
     * Format matches PDF: "Please enter the location of the box to use this SpecialTool: "
     *
     * @param tool the tool being applied
     * @return [row, col] of selected box (0-based indices)
     */
    public int[] selectBoxForTool(SpecialTool tool) {
        while (true) {
            System.out.print("Please enter the location of the box to use this SpecialTool: ");
            String input = scanner.nextLine();

            // Parse position using validator
            int[] position = validator.parsePosition(input);
            if (position == null) {
                System.out.println("INCORRECT INPUT: Invalid format. Please reenter the location: ");
                continue;
            }

            return position;
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
