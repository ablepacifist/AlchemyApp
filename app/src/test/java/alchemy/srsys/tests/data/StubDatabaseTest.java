package alchemy.srsys.tests.data;

import static org.junit.Assert.*;

import java.util.*;
import org.junit.Before;
import org.junit.Test;

import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.object.Effect;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.Ingredient;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;
import alchemy.srsys.object.IPotion;

public class StubDatabaseTest {

    private IStubDatabase db;

    @Before
    public void setup() {
        db = new StubDatabase();
    }

    @Test
    public void testGetNextPotionId() {
        int first = db.getNextPotionId();
        int second = db.getNextPotionId();
        assertEquals("First potion id should be 1", 1, first);
        assertEquals("Second potion id should be 2", 2, second);
    }

    @Test
    public void testGetNextPlayerId() {
        int nextPlayerId = db.getNextPlayerId();
        assertEquals("Next player id should be 3", 3, nextPlayerId);
    }


    @Test
    public void testGetIngredientByName() {
        IIngredient redHerb = db.getIngredientByName("Red Herb");
        assertNotNull("Red Herb should be found", redHerb);
        assertEquals("Red Herb", redHerb.getName());
    }

    @Test
    public void testGetAllIngredients() {
        List<IIngredient> ingredients = db.getAllIngredients();
        // From initialization: Red Herb, Blue Mushroom, Yellow Flower.
        assertEquals("Should have 3 ingredients", 3, ingredients.size());
    }

    @Test
    public void testAddPlayerAndGetPlayer() {
        // Create a new player with id = 2.
        IInventory inv = new Inventory();
        KnowledgeBook kb = new KnowledgeBook(new HashMap<>());
        Player newPlayer = new Player(2, "john", "secret", inv, kb);
        db.addPlayer(newPlayer);

        Player fetched = db.getPlayer(2);
        assertNotNull("Player 'john' should have been added", fetched);
        assertEquals("john", fetched.getUsername());
    }

    @Test
    public void testGetPlayerByUsername() {
        Player player = db.getPlayerByUsername("alex");
        assertNotNull("Player alex should be found", player);
        assertEquals("alex", player.getUsername());
    }

    @Test
    public void testGetAllPlayers() {
        Collection<Player> players = db.getAllPlayers();
        // At minimum, the initial player "alex" exists.
        assertTrue("There should be at least 1 player", players.size() >= 1);
    }

    // --- Inventory Tests ---

    @Test
    public void testAddIngredientToPlayerInventory() {
        // For player "alex" (id = 1), initial quantity of Red Herb is 3.
        IIngredient redHerb = db.getIngredientByName("Red Herb");
        // Add 2 more of Red Herb.
        db.addIngredientToPlayerInventory(1, redHerb, 2);

        IInventory inv = db.getPlayerInventory(1);
        // Use the getIngredients() map from Inventory
        Map<IIngredient, Integer> ingMap = inv.getIngredients();
        Integer quantity = ingMap.get(redHerb);
        assertNotNull("Red Herb quantity should not be null", quantity);
        assertEquals("Red Herb quantity should be updated to 5", 5, quantity.intValue());
    }

    @Test
    public void testRemoveIngredientFromPlayerInventory() {
        // For player "alex" (id = 1), initial quantity of Red Herb is 3.
        IIngredient redHerb = db.getIngredientByName("Red Herb");
        db.removeIngredientFromPlayerInventory(1, redHerb, 1);

        IInventory inv = db.getPlayerInventory(1);
        Map<IIngredient, Integer> ingMap = inv.getIngredients();
        Integer quantity = ingMap.get(redHerb);
        // After removing 1, quantity should be 2.
        assertNotNull("Red Herb quantity should not be null", quantity);
        assertEquals("After removal, Red Herb quantity should be 2", 2, quantity.intValue());
    }

    @Test
    public void testGetPlayerInventory() {
        IInventory inv = db.getPlayerInventory(1);
        assertNotNull("Inventory should not be null", inv);

        Map<IIngredient, Integer> ingMap = inv.getIngredients();
        Integer redHerbQty = ingMap.get(db.getIngredientByName("Red Herb"));
        Integer blueMushroomQty = ingMap.get(db.getIngredientByName("Blue Mushroom"));
        Integer yellowFlowerQty = ingMap.get(db.getIngredientByName("Yellow Flower"));

        assertEquals("Red Herb quantity should be 3", 3, redHerbQty.intValue());
        assertEquals("Blue Mushroom quantity should be 1", 1, blueMushroomQty.intValue());
        assertEquals("Yellow Flower quantity should be 1", 1, yellowFlowerQty.intValue());
    }

    @Test
    public void testAddPotionToPlayerInventory() {
        // Create a dummy potion.
        List<IEffect> effects = Arrays.asList(new Effect(1, "Healing", "Restores health over time."));
        Potion potion = new Potion(100, "Test Potion", effects, null, null);

        db.addPotionToPlayerInventory(1, potion, 2);
        IInventory inv = db.getPlayerInventory(1);
        Map<IPotion, Integer> potionMap = inv.getPotions();
        Integer potionQty = potionMap.get(potion);
        assertNotNull("Potion quantity should not be null", potionQty);
        assertEquals("Potion quantity should be 2", 2, potionQty.intValue());
    }

    // --- Knowledge Book Tests ---
    @Test
    public void testAddKnowledgeEntry() {
        IIngredient redHerb = db.getIngredientByName("Red Herb");
        IEffect healing = new Effect(1, "Healing", "Restores health over time.");

        // Retrieve the player's current knowledge before adding the new entry.
        IKnowledgeBook kbBefore = db.getKnowledgeBook(1);
        Map<Integer, List<IEffect>> mapBefore = kbBefore.getKnowledge();
        List<IEffect> effectsBefore = mapBefore.get(redHerb.getId());
        int countBefore = (effectsBefore == null) ? 0 : effectsBefore.size();

        // Add the new knowledge entry.
        db.addKnowledgeEntry(1, redHerb, healing);

        // Retrieve the updated knowledge book and unwrap the map.
        IKnowledgeBook kbAfter = db.getKnowledgeBook(1);
        Map<Integer, List<IEffect>> mapAfter = kbAfter.getKnowledge();
        List<IEffect> effectsAfter = mapAfter.get(redHerb.getId());
        assertNotNull("Knowledge entry for Red Herb should exist", effectsAfter);
        assertEquals("Knowledge entry count should increase by 1", countBefore + 1, effectsAfter.size());
    }
    @Test
    public void testUpdateKnowledgeBook() {
        // For player 1, update the knowledge book with Blue Mushroom.
        IIngredient blueMushroom = db.getIngredientByName("Blue Mushroom");
        db.updateKnowledgeBook(1, blueMushroom);

        // Get the updated knowledge book and unwrap the map.
        IKnowledgeBook kb = db.getKnowledgeBook(1);
        Map<Integer, List<IEffect>> map = kb.getKnowledge();
        List<IEffect> effects = map.get(blueMushroom.getId());

        // Blue Mushroom was initialized with Poison and Weakness and updated to have an extra effect,
        // therefore we expect 3 effects.
        assertNotNull("Blue Mushroom knowledge should not be null", effects);
        assertEquals("Blue Mushroom should have 3 effects in knowledge", 3, effects.size());
    }
    @Test
    public void testGetKnowledgeBook() {
        // Update knowledge book for Yellow Flower.
        IIngredient yellowFlower = db.getIngredientByName("Yellow Flower");
        db.updateKnowledgeBook(1, yellowFlower);

        // Retrieve the knowledge book and unwrap the map.
        IKnowledgeBook kb = db.getKnowledgeBook(1);
        Map<Integer, List<IEffect>> map = kb.getKnowledge();
        List<IEffect> effects = map.get(yellowFlower.getId());

        // Yellow Flower was initialized with Healing and Poison and processed by update logic,
        // so we expect a total of 3 effects.
        assertNotNull("Yellow Flower knowledge should not be null", effects);
        assertEquals("Yellow Flower should have 3 effects", 3, effects.size());
    }
    @Test
    public void testGetEffectsForIngredient() {
        // For Red Herb (ingredient id 1), expected effects include Healing (id 1) and Strength (id 3).
        List<IEffect> effects = db.getEffectsForIngredient(1);
        // Remove any accidental null values.
        effects.removeIf(Objects::isNull);

        assertEquals("Red Herb should have 3 effects", 3, effects.size());
        boolean hasHealing  = effects.stream().anyMatch(e -> e.getId() == 1);
        boolean hasStrength = effects.stream().anyMatch(e -> e.getId() == 3);
        assertTrue("Red Herb should have Healing effect", hasHealing);
        assertTrue("Red Herb should have Strength effect", hasStrength);
    }

}
