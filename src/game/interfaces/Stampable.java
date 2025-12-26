package game.interfaces;

/**
 * Interface for objects whose top side can be stamped with a letter.
 * Implemented by Box and its subclasses.
 */
public interface Stampable {

    /**
     * Stamps the top side with a new letter.
     * 
     * @param letter The new letter for the top side
     */
    void stampTopSide(char letter);

    /**
     * Checks if the object can be stamped.
     * 
     * @return true if the object can be stamped, false otherwise (e.g.,
     *         UnchangingBox returns false)
     */
    boolean canBeStamped();
}
