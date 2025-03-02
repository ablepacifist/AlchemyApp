package alchemy.srsys.logic;

import alchemy.srsys.object.IPlayerManager;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.Player;

public class PlayerManager implements IPlayerManager {
    private IStubDatabase database;

    public PlayerManager(IStubDatabase database) {
        this.database = database;
    }

    // Registration
    public boolean registerPlayer(String username, String password) {
        // Check if username already exists
        if (database.getPlayerByUsername(username) != null) {
            return false; // Username taken
        }
        // Create new player
        int newId = database.getNextPlayerId();
        Player player = new Player(newId, username, password, database.getAllIngredients());
        database.addPlayer(player);
        return true;
    }

    // Login
    public Player loginPlayer(String username, String password) {
        Player player = database.getPlayerByUsername(username);
        if (player != null && player.getPassword().equals(password)) {
            return player;
        }
        return null; // Login failed
    }
}
