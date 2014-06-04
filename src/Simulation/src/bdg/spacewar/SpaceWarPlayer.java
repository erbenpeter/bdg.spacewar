package bdg.spacewar;

/**
 * A base interface for all player programs.
 * @author Bence
 */
public interface SpaceWarPlayer {
    
    /**
     * This method will be invoked in every round of the game. The simulator module
     * passes the current status using csv format, and the player programs have
     * to return their reaction.
     * @param csv The status before the round
     * @return The reaction in CSV format
     */
    public String round(String csv);
    
    /**
     * This method will be invoked only once, before the game starts. The basic
     * configuration of the game is passed here using CSV format.
     * @param csv The basic configuration of the current game
     */
    public void configuration(String csv);
}
