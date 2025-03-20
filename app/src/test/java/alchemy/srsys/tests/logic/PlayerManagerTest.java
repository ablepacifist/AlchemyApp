package alchemy.srsys.tests.logic;

import static org.junit.Assert.*;


import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Effect;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.Ingredient;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;



import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

public class PlayerManagerTest {
    private IStubDatabase db;
    private PlayerManager playerManager;
    private Player testPlayer;
    private int playerId;

    // Effects used in tests.
    private IEffect sharedEffect;
    private IEffect uniqueEffect1;
    private IEffect uniqueEffect2;

    // Ingredients for different scenarios.
    private IIngredient ingWithShared1;
    private IIngredient ingWithShared2;
    private IIngredient ingNoShared;

    @Before
    public void setUp() {
        // Create a fresh database instance for every test.
        db = new StubDatabase();
        playerManager = new PlayerManager(db);

        // Create our own effects.
        sharedEffect = new Effect(100, "Spark", "Shared spark effect.");
        uniqueEffect1 = new Effect(101, "Glow", "Unique glow effect.");
        uniqueEffect2 = new Effect(102, "Shine", "Unique shine effect.");

        // Add effects to the database.
        db.addEffect(sharedEffect);
        db.addEffect(uniqueEffect1);
        db.addEffect(uniqueEffect2);

        // Create ingredients.
        // Two ingredients that share "Spark" plus one extra unique effect.
        ingWithShared1 = new Ingredient(200, "HerbA", Arrays.asList(sharedEffect, uniqueEffect1));
        ingWithShared2 = new Ingredient(201, "HerbB", Arrays.asList(sharedEffect, uniqueEffect2));
        // An ingredient that does not share any effect with ingWithShared1.
        ingNoShared = new Ingredient(202, "HerbC", Arrays.asList(uniqueEffect2));

        // Add ingredients to the database.
        db.addIngredient(ingWithShared1);
        db.addIngredient(ingWithShared2);
        db.addIngredient(ingNoShared);

        // Create and add a test player with a new, empty inventory and an empty knowledge book.
        playerId = db.getNextPlayerId();
        testPlayer = new Player(playerId, "potionTester", "testpass",
                new Inventory(), new KnowledgeBook(new HashMap<>()));
        db.addPlayer(testPlayer);
    }

    // ----- Existing Tests -----
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

    // ----- Added Tests for Login and Registration -----
    @Test
    public void testLoginPlayerSuccess() {
        // Create a player.
        playerManager.createPlayer("TestLogin", "Password123");
        int newPlayerId = db.getNextPlayerId() - 1;
        Player created = playerManager.getPlayerById(newPlayerId);
        // Now login:
        Player loggedIn = playerManager.loginPlayer("TestLogin", "Password123");
        assertNotNull("Player should be logged in with correct credentials", loggedIn);
        assertEquals("Username should match", "TestLogin", loggedIn.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginPlayerInvalidUsername() {
        // Attempt login with a non-existent username.
        playerManager.loginPlayer("NonExistent", "Whatever");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginPlayerInvalidPassword() {
        playerManager.createPlayer("TestUser", "CorrectPass");
        playerManager.loginPlayer("TestUser", "WrongPass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPlayerPasswordMismatch() {
        playerManager.registerPlayer("MismatchUser", "abc", "def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterPlayerUsernameAlreadyTaken() {
        playerManager.createPlayer("ExistingUser", "pass1");
        playerManager.registerPlayer("ExistingUser", "pass2", "pass2");
    }

    @Test
    public void testRegisterPlayerSuccess() {
        playerManager.registerPlayer("NewUser", "newpass", "newpass");
        Player player = playerManager.getPlayerByUsername("NewUser");
        assertNotNull("Player should be registered successfully", player);
        assertEquals("Username should match", "NewUser", player.getUsername());
    }

    // ----- Added Tests for Foraging -----

    @Test
    public void testForageSuccess() {
        // Ensure that there is at least one ingredient in the master list.
        StubDatabase stubDb = (StubDatabase) db;
        // For this test, add a known ingredient to the master list if not already present.
        if (stubDb.getAllIngredients().isEmpty()) {
            stubDb.addIngredient(ingWithShared1);
        }
        String ingredientName = playerManager.forage(playerId);
        assertNotNull("Forage should return the name of a foraged ingredient", ingredientName);
        assertFalse("Foraged ingredient name should not be empty", ingredientName.isEmpty());
        // Check that the player's inventory contains that ingredient.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        boolean found = false;
        for (Integer qty : inv.getIngredients().values()) {
            if (qty > 0) {
                found = true;
                break;
            }
        }
        assertTrue("Player's inventory should have at least one unit of a foraged ingredient", found);
    }

    // ----- Added Tests for Consuming Ingredients and Potions -----
    @Test
    public void testConsumeIngredientLearnsNewEffect() {
        // Give the player one ingredient with quantity 2.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);

        // Assert that the knowledge for this ingredient is initially empty.
        IKnowledgeBook initialKb = playerManager.getKnowledgeBook(playerId);
        assertFalse("Knowledge for the ingredient should be empty initially",
                initialKb.getKnowledge().containsKey(ingWithShared1.getId()));

        // Consume the ingredient.
        playerManager.consumeIngredient(playerId, ingWithShared1);

        // Verify that inventory quantity is decremented.
        assertEquals("Remaining quantity should be 1 after consumption",
                1, (int) inv.getIngredients().get(ingWithShared1));

        // Get the updated knowledge book.
        IKnowledgeBook kbAfter = playerManager.getKnowledgeBook(playerId);
        List<IEffect> learnedEffects = kbAfter.getKnowledge().get(ingWithShared1.getId());

        // Verify that a new effect is now learned.
        assertNotNull("Knowledge for the ingredient should not be null", learnedEffects);
        assertFalse("Knowledge for the ingredient should not be empty", learnedEffects.isEmpty());
    }



    @Test
    public void testConsumeIngredientAlreadyKnown() {
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);

        // Pre-populate the knowledge book with all effects for this ingredient using the proper API.
        for (IEffect effect : ingWithShared1.getEffects()) {
            db.addKnowledgeEntry(playerId, ingWithShared1, effect);
        }

        // Consume the ingredient.
        playerManager.consumeIngredient(playerId, ingWithShared1);

        // Verify the inventory is decremented.
        assertEquals("Remaining quantity should be 1 after consumption", 1, (int) inv.getIngredients().get(ingWithShared1));

        // Since all effects were already known, the knowledge should remain the same.
        List<IEffect> learnedEffects = playerManager.getKnowledgeBook(playerId).getKnowledge().get(ingWithShared1.getId());
        assertNotNull("Knowledge for the ingredient should not be null", learnedEffects);
        assertEquals("Knowledge list size should remain unchanged", ingWithShared1.getEffects().size(), learnedEffects.size());
    }


    @Test
    public void testConsumePotion() {
        // Create a test potion.
        Potion testPotion = new Potion(300, "Test Potion", Arrays.asList(sharedEffect), ingWithShared1, ingWithShared2);

        // Add the potion to the player's inventory (assume Inventory supports adding potions).
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addPotion(testPotion, 2);

        // Consume the potion.
        playerManager.consumePotion(playerId, testPotion);

        // Verify that the potion's quantity is decremented.
        Map<IPotion, Integer> potionMap = inv.getPotions();
        assertEquals("Potion quantity should be decremented to 1", 1, (int) potionMap.get(testPotion));
    }
}

