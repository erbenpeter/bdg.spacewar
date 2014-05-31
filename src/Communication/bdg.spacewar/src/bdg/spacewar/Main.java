package bdg.spacewar;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A main class for {@code bdg.spacewar} project. It loads 'player' JARs. <b>These
 * JARs must have a {@code bdg.spacewar.player.Player} class, which implements
 * {@link SpacewarPlayer}</b>
 * @author Bence
 */
public class Main {
    
    private static final int NUMBER_OF_PLAYERS = 2;
    private static SpacewarPlayer[] players = new SpacewarPlayer[NUMBER_OF_PLAYERS];
    
    
    
    /**
     * The Java VM invokes this method first. It loads the corresponding JARs
     * which were specified in {@code args}.
     * @param args The paths of the player JARs.
     */
    public static void main(String[] args) {
        if(args.length<NUMBER_OF_PLAYERS) {
            System.out.println("There must be at least "+NUMBER_OF_PLAYERS+" players");
            return;
        }
        String config = Simulator.getConfiguration();
        for(int i=0;i<NUMBER_OF_PLAYERS;i++) {
            players[i] = javaSpacewarPlayer(args[i]);
            players[i].configuration(config);
        }
        
        
        for(int i=0;i<10;i++) {
            for(int j=0;j<NUMBER_OF_PLAYERS;j++)
                players[j].round(Simulator.nextFrame(null));
        }
    }
    
    
    
    
    private static SpacewarPlayer javaSpacewarPlayer(String path) {
        try {
            ClassLoader loader = new URLClassLoader(new URL[] {
                new File(path).toURI().toURL()
            });
            Class<? extends SpacewarPlayer> playerProgram =
                    loader.loadClass("bdg.spacewar.player.Player")
                            .asSubclass(SpacewarPlayer.class);
            return playerProgram.newInstance();
            
            
            
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
