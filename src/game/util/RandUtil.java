package game.util;

import game.boxes.*;
import game.tools.*;

import java.util.*;

/**
 * Utility class for random generation in the game.
 * Provides methods for generating box surfaces, tools, and random selections.
 */
public class RandUtil {
    // Letters used on box surfaces: A through H
    private static final Random random = new Random();
    private static final List<Character> BASE_POOL = Arrays.asList(
            'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D',
            'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H');
    
    // All possible target letters
    private static final char[] LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    /**
     * Generates 6 random surface letters for a box.
     * Each letter (A-H) can appear at most twice on a box.
     *
     * @return array of 6 characters for box surfaces
     */
    public static char[] generateBoxSurfaces() {
        List<Character> tempPool = new ArrayList<>(BASE_POOL);
        Collections.shuffle(tempPool);

        char[] surfaces = new char[6];
        for (int i = 0; i < 6; i++) {
            surfaces[i] = tempPool.get(i);
        }
        return surfaces;
    }

    /**
     * Generates a random letter from A to H.
     *
     * @return a random letter
     */
    public static char generateRandomLetter() {
        return LETTERS[random.nextInt(LETTERS.length)];
    }

    /**
     * Checks if a random event occurs based on a percentage chance.
     *
     * @param percentage the chance (0-100) for the event to occur
     * @return true if the event occurs, false otherwise
     */
    public static boolean checkChance(int percentage) {
        return random.nextInt(100) < percentage;
    }

    /**
     * Generates a random SpecialTool (equal probability for each tool type).
     * 20% chance for each: BoxFixer, BoxFlipper, MassRowStamp, MassColumnStamp, PlusShapeStamp
     *
     * @return a randomly selected SpecialTool
     */
    public static SpecialTool generateRandomTool() {
        int choice = random.nextInt(5);
        switch (choice) {
            case 0: return new BoxFixer();
            case 1: return new BoxFlipper();
            case 2: return new MassRowStamp();
            case 3: return new MassColumnStamp();
            case 4: return new PlusShapeStamp();
            default: return new BoxFixer(); // Should never reach here
        }
    }

    /**
     * Generates a random Box based on type probabilities.
     * 85% RegularBox, 10% UnchangingBox, 5% FixedBox
     *
     * @return a randomly typed Box
     */
    public static Box generateRandomBox() {
        int roll = random.nextInt(100);
        if (roll < 85) {
            return new RegularBox();
        } else if (roll < 95) {
            return new UnchangingBox();
        } else {
            return new FixedBox();
        }
    }

    /**
     * Generates a random target letter for the game (A-H).
     *
     * @return a random target letter
     */
    public static char generateTargetLetter() {
        return LETTERS[random.nextInt(LETTERS.length)];
    }
}
