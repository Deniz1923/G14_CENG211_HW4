package game.tools;

import game.core.BoxGrid;
import game.exceptions.BoxAlreadyFixedException;
import game.exceptions.UnmovableFixedBoxException;

/**
 * Abstract base class for all special tools in the game.
 * <p>
 * Using an abstract class (instead of interface) because:
 * - Tools are "objects" that can be stored in boxes and used by players
 * - Abstract class can provide shared behavior and state if needed
 * - Follows the same pattern as Box (abstract class for objects)
 * - Per PDF point 11: "determine appropriate use of interfaces vs abstract
 * classes when representing objects"
 * <p>
 * Tool Types (5 total, each with 20% equal probability):
 * - BoxFixer: Converts a box into a FixedBox
 * - BoxFlipper: Flips a box upside down
 * - MassRowStamp: Stamps all boxes in a row with target letter
 * - MassColumnStamp: Stamps all boxes in a column with target letter
 * - PlusShapeStamp: Stamps 5 boxes in a plus pattern
 * <p>
 * All tools are single-use and must be applied immediately when acquired.
 */
public abstract class SpecialTool {

    // Name of the tool for display purposes
    private final String name;

    /**
     * Constructs a SpecialTool with the given name.
     *
     * @param name the display name of the tool
     */
    protected SpecialTool(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the tool for display purposes.
     *
     * @return the tool name (e.g., "BoxFixer", "MassRowStamp")
     */
    public String getName() {
        return name;
    }

    /**
     * Applies the tool's effect to the grid at the specified position.
     * Different tools have different effects and restrictions.
     * <p>
     * This method uses generics to allow subclasses to specify their return type.
     *
     * @param grid         the box grid to modify
     * @param targetLetter the target letter for stamp tools
     * @param row          the row of the selected box (0-based)
     * @param col          the column of the selected box (0-based)
     * @throws BoxAlreadyFixedException   if BoxFixer is used on a FixedBox
     * @throws UnmovableFixedBoxException if BoxFlipper is used on a FixedBox
     */
    public abstract void apply(BoxGrid grid, char targetLetter, int row, int col)
            throws BoxAlreadyFixedException, UnmovableFixedBoxException;

    /**
     * Returns the prompt text for user input when using this tool.
     * MassRowStamp prompts for row, MassColumnStamp for column, others for
     * position.
     *
     * @return the usage prompt string
     */
    public abstract String getUsagePrompt();

    /**
     * Returns a string representation of this tool.
     *
     * @return the tool name
     */
    @Override
    public String toString() {
        return name;
    }
}
