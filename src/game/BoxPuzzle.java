package game;

import game.boxes.Box;
import game.boxes.FixedBox;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.EmptyBoxException;
import game.exceptions.UnmovableFixedBoxException;
import game.tools.SpecialTool;
import game.util.RandUtil;

import java.util.Scanner;

/**
 * Main game controller for the Box Top Side Matching Puzzle.
 * Manages the game flow, turn structure, and player interactions.
 * Uses an inner class (GameMenu) for handling menu operations.
 */
public class BoxPuzzle {
    private static final int TOTAL_TURNS = 5;
    
    private final BoxGrid grid;
    private final char targetLetter;
    private final GameMenu menu;
    private int currentTurn;

    /**
     * Constructs a new BoxPuzzle game and starts gameplay.
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
     */
    private void startGame() {
        menu.displayWelcome();
        menu.displayGrid();
        
        while (currentTurn <= TOTAL_TURNS) {
            // FIRST STAGE: Edge box selection and rolling
            menu.displayFirstStageHeader(currentTurn);
            
            try {
                int[] edgePosition = menu.selectEdgeBox();
                if (edgePosition == null) {
                    // Player chose to view box net, don't consume turn
                    continue;
                }
                
                int edgeRow = edgePosition[0];
                int edgeCol = edgePosition[1];
                
                grid.rollFromEdge(edgeRow, edgeCol);
                menu.displayRollSuccess(edgeRow, edgeCol);
                menu.displayGrid();
                
                // SECOND STAGE: Box opening for tool
                menu.displaySecondStageHeader(currentTurn);
                int[] boxPosition = menu.selectBoxToOpen(edgeRow, edgeCol);
                int boxRow = boxPosition[0];
                int boxCol = boxPosition[1];
                
                SpecialTool tool = openBox(boxRow, boxCol);
                menu.displayToolAcquired(tool, boxRow, boxCol);
                
                // Tool usage (if tool was acquired)
                if (tool != null) {
                    boolean useTool = menu.askToUseTool(tool);
                    if (useTool) {
                        int[] toolPosition = menu.selectBoxForTool();
                        try {
                            tool.apply(grid, targetLetter, toolPosition[0], toolPosition[1]);
                            menu.displayToolUsed(tool, toolPosition[0], toolPosition[1]);
                        } catch (BoxAlreadyFixedException | UnmovableFixedBoxException e) {
                            menu.displayError(e.getMessage());
                        }
                    }
                }
                
                menu.displayGrid();
                
            } catch (UnmovableFixedBoxException e) {
                menu.displayError(e.getMessage());
                menu.displayTurnWasted();
            } catch (EmptyBoxException e) {
                menu.displayError(e.getMessage());
                menu.displayTurnWasted();
            }
            
            currentTurn++;
        }
        
        endGame();
    }

    /**
     * Opens a box and retrieves its tool.
     *
     * @param row the row of the box
     * @param col the column of the box
     * @return the tool inside, or null if empty
     * @throws EmptyBoxException if the box is empty
     */
    private SpecialTool openBox(int row, int col) throws EmptyBoxException {
        Box box = grid.getBox(row, col);
        
        if (box.isOpen()) {
            throw new EmptyBoxException("Box at R" + (row + 1) + "-C" + (col + 1) + " has already been opened!");
        }
        
        box.setOpen(true);
        SpecialTool tool = box.getTool();
        
        if (tool == null || box.isEmpty()) {
            throw new EmptyBoxException("Box at R" + (row + 1) + "-C" + (col + 1) + " is empty!");
        }
        
        // Remove tool from box after acquiring
        box.setTool(null);
        return tool;
    }

    /**
     * Ends the game and displays final results.
     */
    private void endGame() {
        menu.displayGameEnd();
    }

    /**
     * Inner class handling all menu operations and user interactions.
     * Follows the spec requirement to use an inner class for menu handling.
     */
    private class GameMenu {
        private final Scanner scanner;

        public GameMenu() {
            this.scanner = new Scanner(System.in);
        }

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

        public void displayFirstStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - FIRST STAGE:");
            System.out.println();
        }

        public void displaySecondStageHeader(int turn) {
            System.out.println("---> TURN " + turn + " - SECOND STAGE:");
            System.out.println();
        }

        public void displayGrid() {
            System.out.println(grid.toString());
        }

        /**
         * Prompts user to select an edge box for rolling.
         * Allows viewing a box net before selection.
         *
         * @return [row, col] array of selected edge box, or null if viewing net
         */
        public int[] selectEdgeBox() {
            while (true) {
                System.out.println("Enter the edge box you want to roll (format: R1-C5) or VIEW to see a box net:");
                String input = scanner.nextLine().trim().toUpperCase();
                
                if (input.equals("VIEW")) {
                    viewBoxNet();
                    return null;
                }
                
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# (e.g., R1-C5)");
                    continue;
                }
                
                int row = position[0];
                int col = position[1];
                
                if (!grid.isEdge(row, col)) {
                    System.out.println("Selected box is not on the edge! Choose from row 1, row 8, column 1, or column 8.");
                    continue;
                }
                
                return position;
            }
        }

        /**
         * Displays the net of a user-selected box.
         */
        private void viewBoxNet() {
            System.out.println("Enter box position to view (format: R1-C5):");
            String input = scanner.nextLine().trim().toUpperCase();
            
            int[] position = parsePosition(input);
            if (position == null) {
                System.out.println("Invalid format!");
                return;
            }
            
            System.out.println();
            System.out.println("Box at R" + (position[0] + 1) + "-C" + (position[1] + 1) + ":");
            System.out.println(grid.getBoxNet(position[0], position[1]));
            System.out.println();
        }

        /**
         * Prompts user to select a box to open from the rolled row/column.
         *
         * @param edgeRow the row of the edge box that initiated rolling
         * @param edgeCol the column of the edge box that initiated rolling
         * @return [row, col] of selected box
         */
        public int[] selectBoxToOpen(int edgeRow, int edgeCol) {
            while (true) {
                System.out.println("Select a box from the rolled row/column to open (format: R1-C5):");
                String input = scanner.nextLine().trim().toUpperCase();
                
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# (e.g., R1-C5)");
                    continue;
                }
                
                int row = position[0];
                int col = position[1];
                
                // Check if the box is in the same row or column as the edge
                boolean validSelection = false;
                if (edgeRow == 0 || edgeRow == BoxGrid.GRID_SIZE - 1) {
                    // Rolled vertically, must be same column
                    validSelection = (col == edgeCol);
                } else {
                    // Rolled horizontally, must be same row
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
         *
         * @return [row, col] of selected box
         */
        public int[] selectBoxForTool() {
            while (true) {
                System.out.println("Select a box to apply the tool (format: R1-C5):");
                String input = scanner.nextLine().trim().toUpperCase();
                
                int[] position = parsePosition(input);
                if (position == null) {
                    System.out.println("Invalid format! Use R#-C# (e.g., R1-C5)");
                    continue;
                }
                
                return position;
            }
        }

        /**
         * Asks if the player wants to use the acquired tool.
         *
         * @param tool the acquired tool
         * @return true if player wants to use it
         */
        public boolean askToUseTool(SpecialTool tool) {
            System.out.println("Do you want to use " + tool.getName() + "? (Y/N):");
            String input = scanner.nextLine().trim().toUpperCase();
            return input.equals("Y") || input.equals("YES");
        }

        public void displayRollSuccess(int row, int col) {
            String direction = grid.getRollDirection(row, col);
            System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) + 
                    " has been flipped " + direction + ".");
            System.out.println();
        }

        public void displayToolAcquired(SpecialTool tool, int row, int col) {
            if (tool != null) {
                System.out.println("The box on location R" + (row + 1) + "-C" + (col + 1) + 
                        " is opened. It contains a SpecialTool --> " + tool.getName());
                System.out.println();
            }
        }

        public void displayToolUsed(SpecialTool tool, int row, int col) {
            System.out.println(tool.getName() + " applied at R" + (row + 1) + "-C" + (col + 1) + "!");
            System.out.println();
        }

        public void displayError(String message) {
            System.out.println("ERROR: " + message);
        }

        public void displayTurnWasted() {
            System.out.println("Turn wasted! Continuing to next turn...");
            System.out.println();
        }

        public void displayGameEnd() {
            System.out.println();
            System.out.println("The final state of the box grid:");
            displayGrid();
            
            int matchCount = grid.countMatchingBoxes(targetLetter);
            
            System.out.println("********* GAME OVER *********");
            System.out.println();
            System.out.println("THE TOTAL NUMBER OF TARGET LETTER \"" + targetLetter + 
                    "\" IN THE BOX GRID --> " + matchCount);
            System.out.println();
            System.out.println("The game has been SUCCESSFULLY completed!");
        }

        /**
         * Parses a position string in format "R#-C#" to [row, col] array.
         *
         * @param input the position string (case-insensitive)
         * @return [row, col] as 0-based indices, or null if invalid
         */
        private int[] parsePosition(String input) {
            try {
                // Format: R1-C5 or r1-c5
                String[] parts = input.split("-");
                if (parts.length != 2) return null;
                
                if (!parts[0].startsWith("R") || !parts[1].startsWith("C")) return null;
                
                int row = Integer.parseInt(parts[0].substring(1)) - 1;
                int col = Integer.parseInt(parts[1].substring(1)) - 1;
                
                if (row < 0 || row >= BoxGrid.GRID_SIZE || col < 0 || col >= BoxGrid.GRID_SIZE) {
                    return null;
                }
                
                return new int[]{row, col};
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
