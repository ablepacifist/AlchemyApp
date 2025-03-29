package alchemy.srsys.logic;

import alchemy.srsys.application.MyApp;
import alchemy.srsys.data.IStubDatabase;


public class GameManager implements GameManagerService {
    // Single instance (or you could use dependency injection to avoid singletons)
    private static GameManager instance;

    private final PlayerManager playerManager;
    private final PotionManager potionManager;

    // Private constructor that requires pre-built manager instances.
    private GameManager(PlayerManager playerManager, PotionManager potionManager) {
        this.playerManager = playerManager;
        this.potionManager = potionManager;
    }

    /**
     * Get an instance of GameManager. This method is a simple factory.
     */
    public static GameManager getInstance(boolean isProduction) {
        if (instance == null) {
            IStubDatabase db = MyApp.getDatabase(isProduction);
            // Create independent manager instances.
            PlayerManager pm = new PlayerManager(db);
            PotionManager pom = new PotionManager(db);
            instance = new GameManager(pm, pom);
        }
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PotionManager getPotionManager() {
        return potionManager;
    }

    @Override
    public void startGame() {
        // Perform any game start initializations.
        System.out.println("Game started!");
    }

    @Override
    public void endGame() {
        // Perform cleanup tasks.
        System.out.println("Game ended!");
    }
    public String forage(int playerId) {
        return playerManager.forage(playerId);
    }


}
