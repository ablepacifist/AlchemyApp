package alchemy.srsys.tests.logic;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.logic.PotionManager;
import alchemy.srsys.object.Effect;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.Ingredient;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;


public class PotionManagerTest {
    private IStubDatabase db;
    private PotionManager potionManager;
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
        // Create a fresh database.
        db = new StubDatabase();
        potionManager = new PotionManager(db);

        // Create our own effects.
        sharedEffect = new Effect(100, "Spark", "Shared spark effect.");
        uniqueEffect1 = new Effect(101, "Glow", "Unique glow effect.");
        uniqueEffect2 = new Effect(102, "Shine", "Unique shine effect.");

        // Add effects to the database (if your StubDatabase implementation supports it).
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

        // Create and add a test player with a new, empty inventory and a knowledge book.
        playerId = db.getNextPlayerId();
        testPlayer = new Player(playerId, "potionTester", "testpass", new Inventory(), new KnowledgeBook(new java.util.HashMap<>()));
        db.addPlayer(testPlayer);
    }

    @Test
    public void testBrewPotionSuccess() {
        // Add the two ingredients (that share Spark) to the player's inventory.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);
        inv.addIngredient(ingWithShared2, 2);

        // Attempt to brew a potion.
        Potion potion = potionManager.brewPotion(playerId, ingWithShared1, ingWithShared2);
        assertNotNull("Potion should be brewed when ingredients share effects", potion);
        // The potion's name should include "Spark".
        assertTrue("Potion's name should contain 'Spark'", potion.getName().contains("Spark"));

        // Inventory should have been decremented (each ingredient used one unit).
        Map<IIngredient, Integer> ingMap = inv.getIngredients();
        assertEquals("Remaining quantity of ingWithShared1 should be 1", 1, (int)ingMap.get(ingWithShared1));
        assertEquals("Remaining quantity of ingWithShared2 should be 1", 1, (int)ingMap.get(ingWithShared2));
    }

    @Test
    public void testBrewPotionMissingIngredient() {
        // Only add one of the ingredients.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);
        // Do NOT add ingWithShared2.
        Potion potion = potionManager.brewPotion(playerId, ingWithShared1, ingWithShared2);
        assertNull("Potion should not be brewed if one ingredient is missing", potion);
    }

    @Test
    public void testBrewPotionNoSharedEffects() {
        // Add two ingredients that do not share any effect.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);
        inv.addIngredient(ingNoShared, 2);

        // Attempt to brew a potion.
        Potion potion = potionManager.brewPotion(playerId, ingWithShared1, ingNoShared);
        assertNull("Potion should not be brewed if ingredients do not share an effect", potion);

        // Even if brewing fails, the method removes one unit from each ingredient.
        // Confirm inventory updates.
        assertEquals("Remaining quantity of ingWithShared1 should be 1", 1, (int)inv.getIngredients().get(ingWithShared1));
        assertEquals("Remaining quantity of ingNoShared should be 1", 1, (int)inv.getIngredients().get(ingNoShared));
    }


    @Test
    public void testBrewPotionSameIngredient() {
        // Add one ingredient to the player's inventory.
        Inventory inv = (Inventory) db.getPlayerInventory(playerId);
        inv.addIngredient(ingWithShared1, 2);

        // Attempt to brew a potion using the same ingredient (by passing it twice).
        Potion potion = potionManager.brewPotion(playerId, ingWithShared1, ingWithShared1);
        assertNull("Potion should not be brewed when both ingredients are the same", potion);
    }
}
