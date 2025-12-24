package game.util;

import game.boxes.*;
import game.tools.*;

import java.util.*;

/**
 * Utility class for random generation in the game.
 * Provides methods for generating box surfaces, tools, boxes, and random selections.
 * 
 * Uses a singleton Random instance for all random operations.
 */
public class RandUtil {
    // Single Random instance used for all random generation
    private static final Random random = new Random();
    
    // Pool of letters for box surfaces
    // Each letter A-H appears exactly twice (allows max 2 of same letter per box)
    private static final List<Character> BASE_POOL = Arrays.asList(
            'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D',
            'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H');
    
    // All possible target letters (A through H)
    private static final char[] LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    /**
     * Generates 6 random surface letters for a box.
     * Each letter (A-H) can appear at most twice on a box.
     * 
     * Algorithm:
     *   1. Creates a copy of the BASE_POOL (16 letters: 2 each of A-H)
     *   2. Shuffles the pool randomly
     *   3. Takes the first 6 letters as box surfaces
     * 
     * @return array of 6 characters for box surfaces
     */
    public static char[] generateBoxSurfaces() {
        // Create a copy of the base pool and shuffle it
        List<Character> tempPool = new ArrayList<>(BASE_POOL);
        Collections.shuffle(tempPool);

        // Take first 6 letters from shuffled pool
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
     * Generates a random SpecialTool (equal probability for each type).
     * 20% chance for each: BoxFixer, BoxFlipper, MassRowStamp, MassColumnStamp, PlusShapeStamp
     * 
     * @return a randomly selected SpecialTool instance
     */
    public static SpecialTool generateRandomTool() {
        int choice = random.nextInt(5);  // 0-4, each with 20% chance
        switch (choice) {
            case 0: return new BoxFixer();
            case 1: return new BoxFlipper();
            case 2: return new MassRowStamp();
            case 3: return new MassColumnStamp();
            case 4: return new PlusShapeStamp();
            default: return new BoxFixer(); // Fallback (should never reach)
        }
    }

    /**
     * Generates a random Box based on type probabilities.
     * 
     * Probabilities:
     *   - 85% RegularBox (most common, can be fully manipulated)
     *   - 10% UnchangingBox (immune to stamping, guaranteed tool)
     *   - 5% FixedBox (immovable, always empty)
     * 
     * @return a randomly typed Box instance
     */
    public static Box generateRandomBox() {
        int roll = random.nextInt(100);  // 0-99
        if (roll < 85) {
            return new RegularBox();     // 0-84: 85% chance
        } else if (roll < 95) {
            return new UnchangingBox();  // 85-94: 10% chance
        } else {
            return new FixedBox();       // 95-99: 5% chance
        }
    }

    /**
     * Generates a random target letter for the game (A-H).
     * Called once at game start to select the letter players try to match.
     * 
     * @return a random target letter
     */
    public static char generateTargetLetter() {
        return LETTERS[random.nextInt(LETTERS.length)];
    }
}
