package bdg.spacewar;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A main class for {@code bdg.spacewar} project. It loads 'player' JARs. <b>These
 * JARs must have a {@code bdg.spacewar.player.Player} class, which implements
 * {@link SpaceWarPlayer}</b>
 * @author Bence
 */
public class Communication {
    
    private static final int NUMBER_OF_PLAYERS = 2;
    private static SpaceWarPlayer[] players = new SpaceWarPlayer[NUMBER_OF_PLAYERS];
    
    
    
    /**
     * The Java VM invokes this method first. It loads the corresponding JARs
     * which were specified in {@code args}.
     * @param args The paths of the player JARs.
     */
    public static void main(String[] args) {
    	args = new String[]{"player1.jar", "player1.jar"};
        if(args.length<NUMBER_OF_PLAYERS) {
            System.out.println("There must be at least "+NUMBER_OF_PLAYERS+" players");
            return;
        }
        SpaceWar.playerCount = NUMBER_OF_PLAYERS;
        
        
        String config = SpaceWar.BenceGetConfiguration();
        for(int i=0;i<NUMBER_OF_PLAYERS;i++) {
            players[i] = javaSpacewarPlayer(args[i]);
            players[i].configuration(config);
        }
        
        String[] playerAction = new String[NUMBER_OF_PLAYERS];
        String status;
        
        
        boolean first = true;
        do {
        	status = first ? null : SpaceWar.getStatus();
            for(int j=0;j<NUMBER_OF_PLAYERS;j++) {
            	try {
            		playerAction[j] = players[j].round(status);
            	} catch(Throwable t) {
            		t.printStackTrace();
            		playerAction[j] = null;
            	}
            }
            first = false;
            SpaceWar.BenceUpdate(playerAction);
        } while(SpaceWar.status==Constants.STATUS_RUN);
        SpaceWar.BenceEnd();
    }
    
    
    
    
    private static SpaceWarPlayer javaSpacewarPlayer(String path) {
        try {
            URLClassLoader loader = new URLClassLoader(new URL[] {
                new File(path).toURI().toURL()
            });
            Class<? extends SpaceWarPlayer> playerProgram =
                    loader.loadClass("bdg.spacewar.player.Player")
                            .asSubclass(SpaceWarPlayer.class);
            loader.close();
            return playerProgram.newInstance();
            
            
            
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
