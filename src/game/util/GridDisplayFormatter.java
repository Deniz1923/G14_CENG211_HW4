package game.util;

import game.boxes.Box;
import game.boxes.FixedBox;
import game.core.BoxGrid;

/**
 * Utility class for formatting the grid display for console output.
 * Provides methods to format the entire grid, individual boxes, and cube
 * diagrams.
 */
public class GridDisplayFormatter {
    private static final int GRID_SIZE = 8;
    private static final String HORIZONTAL_LINE = " -----------------------------------------------------------------";
    private static final String CELL_FORMAT = " %c-%c-%c |";

    /**
     * Private constructor to prevent instantiation.
     */
    private GridDisplayFormatter() {
    }

    /**
     * Returns a formatted string representation of the entire grid.
     * Includes column headers (C1-C8) and row labels (R1-R8).
     * 
     * @param grid The BoxGrid to format
     * @return The formatted grid string
     */
    public static String formatGrid(BoxGrid grid) {
        StringBuilder sb = new StringBuilder();

        // Column headers
        sb.append("       C1      C2      C3      C4      C5      C6      C7      C8\n");
        sb.append(HORIZONTAL_LINE).append("\n");

        // Grid rows
        for (int row = 0; row < GRID_SIZE; row++) {
            sb.append("R").append(row + 1).append(" |");

            for (int col = 0; col < GRID_SIZE; col++) {
                Box box = grid.getBox(row, col);
                char type = box.getTypeChar();
                char topSide = box.getTopSide();
                // Status: O if opened or FixedBox, M if mystery (unopened)
                char status = (box.hasBeenOpened() || box instanceof FixedBox) ? 'O' : 'M';
                sb.append(String.format(CELL_FORMAT, type, topSide, status));
            }

            sb.append("\n");
            sb.append(HORIZONTAL_LINE).append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns a formatted representation of a single box.
     * Format: "TYPE-LETTER-CONTENT" (e.g., "R-E-M")
     * 
     * @param box The box to format
     * @return The formatted box string
     */
    public static String formatBox(Box box) {
        char type = box.getTypeChar();
        char topSide = box.getTopSide();
        char status = (box.hasBeenOpened() || box instanceof FixedBox) ? 'O' : 'M';
        return String.format("%c-%c-%c", type, topSide, status);
    }

    /**
     * Returns an ASCII cube diagram showing all 6 surfaces of a box.
     * 
     * @param box The box to display
     * @return The formatted cube diagram
     */
    public static String formatBoxNet(Box box) {
        char[] surfaces = box.getAllSurfaces();
        // Index Mapping: 0=Top, 1=Bottom, 2=Front, 3=Back, 4=Left, 5=Right

        return "    -----\n" +
                String.format("    | %c |\n", surfaces[3]) + // Back
                "-------------\n" +
                String.format("| %c | %c | %c |\n", surfaces[4], surfaces[0], surfaces[5]) + // Left, Top, Right
                "-------------\n" +
                String.format("    | %c |\n", surfaces[2]) + // Front
                "    -----\n" +
                String.format("    | %c |\n", surfaces[1]) + // Bottom
                "    -----";
    }

    /**
     * Returns a simplified horizontal line for the grid.
     * 
     * @return The horizontal line string
     */
    public static String getHorizontalLine() {
        return HORIZONTAL_LINE;
    }
}
