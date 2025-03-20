package alchemy.srsys.tests.logic;

import static org.junit.Assert.*;


import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;



public class PlayerManagerTest {
    private IStubDatabase db;
    private PlayerManager playerManager;

    @Before
    public void setUp() {
        // Create a fresh database instance for every test.
        db = new StubDatabase();
        playerManager = new PlayerManager(db);
    }

    @Test
    public void testCreatePlayer() {
        String username = "TestUser";
        String password = "TestPass";
        playerManager.createPlayer(username, password);

        // Because createPlayer() calls db.getNextPlayerId() internally,
        // our expected player ID is the last generated one, which is currentNext - 1.
        int expectedPlayerId = db.getNextPlayerId() - 1;
        Player player = db.getPlayer(expectedPlayerId);

        assertNotNull("Player should be created", player);
        assertEquals("Username should match", username, player.getUsername());
        assertEquals("Password should match", password, player.getPassword());
    }

    @Test
    public void testGetPlayerById() {
        // Create a new player.
        playerManager.createPlayer("User1", "Pass1");
        int playerId = db.getNextPlayerId() - 1;

        Player player = playerManager.getPlayerById(playerId);
        assertNotNull("Player should be retrieved by ID", player);
        assertEquals("Username should match", "User1", player.getUsername());
    }

    @Test
    public void testGetPlayerByUsername() {
        playerManager.createPlayer("Alice", "secret");
        Player player = playerManager.getPlayerByUsername("Alice");

        assertNotNull("Player should be retrieved by username", player);
        assertEquals("Password should match", "secret", player.getPassword());
    }

    @Test
    public void testGetInventory() {
        playerManager.createPlayer("Bob", "pass");
        int playerId = db.getNextPlayerId() - 1;

        Inventory inv = (Inventory) playerManager.getInventory(playerId);
        assertNotNull("Inventory should not be null", inv);
        // New inventory should be empty.
        assertTrue("Inventory should be empty initially", inv.getIngredients().isEmpty());
    }
}
