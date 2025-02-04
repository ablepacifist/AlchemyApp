package alchemy.srsys.ui;

import alchemy.srsys.object.IPlayerManager;

public class AppDataHolder {

    private static AppDataHolder instance;

    private IPlayerManager playerManager;

    private AppDataHolder() {
    }

    public static synchronized AppDataHolder getInstance() {
        if (instance == null) {
            instance = new AppDataHolder();
        }
        return instance;
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(IPlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}
