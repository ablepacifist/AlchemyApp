package alchemy.srsys.tests.data;

import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.object.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class StubDatabaseTest {

    private StubDatabase stubDatabase;

    @Before
    public void setUp() {
        stubDatabase = new StubDatabase();
    }

    // Test for addEffect (private in original code, we need to make it public or test indirectly)
    @Test
    public void testAddAndRetrieveEffect() {
        IEffect newEffect = new Effect(5, "Invisibility");
        stubDatabase.addEffect(newEffect);
        IEffect retrievedById = stubDatabase.getEffectById(5);
        IEffect retrievedByTitle = stubDatabase.getEffectByTitle("Invisibility");
        assertNotNull(retrievedById);
        assertNotNull(retrievedByTitle);
        assertEquals(newEffect, retrievedById);
        assertEquals(newEffect, retrievedByTitle);
    }

    // Test for addIngredient (private in original code, we need to make it public or test indirectly)
    @Test
    public void testAddAndRetrieveIngredient() {
        IEffect healing = stubDatabase.getEffectById(1);
        IEffect strength = stubDatabase.getEffectById(3);
        IIngredient newIngredient = new Ingredient(4, "Golden Apple", Arrays.asList(
                healing,
                strength,
                null,
                null
        ));
        stubDatabase.addIngredient(newIngredient);
        IIngredient retrievedById = stubDatabase.getIngredientById(4);
        IIngredient retrievedByName = stubDatabase.getIngredientByName("Golden Apple");
        assertNotNull(retrievedById);
        assertNotNull(retrievedByName);
        assertEquals(newIngredient, retrievedById);
        assertEquals(newIngredient, retrievedByName);
    }

    @Test
    public void testGetAllEffects() {
        List<IEffect> effects = stubDatabase.getAllEffects();
        assertEquals(4, effects.size());
        IEffect newEffect = new Effect(5, "Invisibility");
        stubDatabase.addEffect(newEffect);
        effects = stubDatabase.getAllEffects();
        assertEquals(5, effects.size());
        assertTrue(effects.contains(newEffect));
    }

    @Test
    public void testGetAllIngredients() {
        List<IIngredient> ingredients = stubDatabase.getAllIngredients();
        assertEquals(3, ingredients.size());
        IEffect healing = stubDatabase.getEffectById(1);
        IIngredient newIngredient = new Ingredient(5, "Silver Leaf", Arrays.asList(
                healing,
                null,
                null,
                null
        ));
        stubDatabase.addIngredient(newIngredient);
        ingredients = stubDatabase.getAllIngredients();
        assertEquals(4, ingredients.size());
        assertTrue(ingredients.contains(newIngredient));
    }

    @Test
    public void testAddPlayerAndGetPlayer() {
        Player player = new Player(1, "Alchemist", stubDatabase.getAllIngredients());
        stubDatabase.addPlayer(player);
        Player retrievedPlayer = stubDatabase.getPlayer(1);
        assertNotNull(retrievedPlayer);
        assertEquals(player, retrievedPlayer);
    }

    @Test
    public void testGetAllPlayers() {
        Player player1 = new Player(1, "Mage", stubDatabase.getAllIngredients());
        Player player2 = new Player(2, "Healer", stubDatabase.getAllIngredients());
        stubDatabase.addPlayer(player1);
        stubDatabase.addPlayer(player2);
        Collection<Player> players = stubDatabase.getAllPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player1));
        assertTrue(players.contains(player2));
    }

    @Test
    public void testGetNextPotionId() {
        int firstId = stubDatabase.getNextPotionId();
        int secondId = stubDatabase.getNextPotionId();
        int thirdId = stubDatabase.getNextPotionId();
        assertEquals(1, firstId);
        assertEquals(2, secondId);
        assertEquals(3, thirdId);
    }

    @Test
    public void testGetIngredientById() {
        IIngredient ingredient = stubDatabase.getIngredientById(1);
        assertNotNull(ingredient);
        assertEquals("Red Herb", ingredient.getName());
    }

    @Test
    public void testGetIngredientByName() {
        IIngredient ingredient = stubDatabase.getIngredientByName("Blue Mushroom");
        assertNotNull(ingredient);
        assertEquals(2, ingredient.getId());
    }

    @Test
    public void testGetEffectById() {
        IEffect effect = stubDatabase.getEffectById(3);
        assertNotNull(effect);
        assertEquals("Strength", effect.getTitle());
    }

    @Test
    public void testGetEffectByTitle() {
        IEffect effect = stubDatabase.getEffectByTitle("Weakness");
        assertNotNull(effect);
        assertEquals(4, effect.getId());
    }

    @Test
    public void testFindIngredientByName() {
        IIngredient ingredient = stubDatabase.findIngredientByName("Yellow Flower");
        assertNotNull(ingredient);
        assertEquals(3, ingredient.getId());
    }

    @Test
    public void testFindEffectById() {
        IEffect effect = stubDatabase.findEffectById(2);
        assertNotNull(effect);
        assertEquals("Poison", effect.getTitle());
    }

    @Test
    public void testAddIngredientToInventory() {
        IIngredient ingredient = stubDatabase.getIngredientById(1);
        stubDatabase.addIngredientToInventory(ingredient);
        List<IIngredient> inventory = stubDatabase.getInventoryIngredients();
        assertEquals(1, inventory.size());
        assertTrue(inventory.contains(ingredient));
    }

    @Test
    public void testRemoveIngredientFromInventory() {
        IIngredient ingredient = stubDatabase.getIngredientById(1);
        stubDatabase.addIngredientToInventory(ingredient);
        stubDatabase.removeIngredientFromInventory(ingredient);
        List<IIngredient> inventory = stubDatabase.getInventoryIngredients();
        assertFalse(inventory.contains(ingredient));
    }

    @Test
    public void testGetInventoryIngredients() {
        IIngredient ingredient1 = stubDatabase.getIngredientById(1);
        IIngredient ingredient2 = stubDatabase.getIngredientById(2);
        stubDatabase.addIngredientToInventory(ingredient1);
        stubDatabase.addIngredientToInventory(ingredient2);
        List<IIngredient> inventory = stubDatabase.getInventoryIngredients();
        assertEquals(2, inventory.size());
        assertTrue(inventory.contains(ingredient1));
        assertTrue(inventory.contains(ingredient2));
    }

    @Test
    public void testUpdateKnowledgeBook() {
        IIngredient ingredient = stubDatabase.getIngredientById(1);
        stubDatabase.updateKnowledgeBook(ingredient);
        Map<String, IEffect[]> knowledgeBook = stubDatabase.getKnowledgeBook();
        assertTrue(knowledgeBook.containsKey("Red Herb"));
        IEffect[] effects = knowledgeBook.get("Red Herb");
        assertNotNull(effects);
        assertEquals(4, effects.length); // Assuming the effects array has size 4
        assertEquals("Healing", effects[0].getTitle());
        assertEquals("Strength", effects[1].getTitle());
    }

    @Test
    public void testGetKnowledgeBook() {
        IIngredient ingredient1 = stubDatabase.getIngredientById(1);
        IIngredient ingredient2 = stubDatabase.getIngredientById(2);
        stubDatabase.updateKnowledgeBook(ingredient1);
        stubDatabase.updateKnowledgeBook(ingredient2);
        Map<String, IEffect[]> knowledgeBook = stubDatabase.getKnowledgeBook();
        assertEquals(2, knowledgeBook.size());
        assertTrue(knowledgeBook.containsKey("Red Herb"));
        assertTrue(knowledgeBook.containsKey("Blue Mushroom"));
    }

    @Test
    public void testGetAllEffectsAfterLoading() {
        stubDatabase.loadEffects("effects.txt");
        List<IEffect> effects = stubDatabase.getAllEffects();
        assertTrue(effects.size() > 4); // Since we added effects
    }

    @Test
    public void testGetAllIngredientsAfterLoading() {
        stubDatabase.loadIngredients("ingredients.txt");
        List<IIngredient> ingredients = stubDatabase.getAllIngredients();
        assertTrue(ingredients.size() > 3); // Since we added ingredients
    }
}
