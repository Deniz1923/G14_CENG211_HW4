package game.util;

import java.util.*;

public class RandUtil {
    //A,B,C,D,E,F,G,H
    //1,2,3,4,5,6,7,8
    private static final Random random = new Random();
    private static final List<Character> BASE_POOL = Arrays.asList(
            'A', 'A', 'B', 'B', 'C', 'C', 'D', 'D',
            'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H');

    public static char[] generateBoxSurfaces() {
        List<Character> tempPool = new ArrayList<>(BASE_POOL);
        Collections.shuffle(tempPool);

        char[] surfaces = new char[6];
        for (int i = 0; i < 6; i++) {
            surfaces[i] = tempPool.get(i);
        }
        return surfaces;
    }

    public static char generateRandomLetter() {
        return BASE_POOL.get(random.nextInt((BASE_POOL.size())));
    }

    public static boolean checkChance(int percentage) {
        return random.nextInt(100) < percentage;
    }

}
