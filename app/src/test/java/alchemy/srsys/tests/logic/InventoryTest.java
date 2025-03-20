package alchemy.srsys.tests.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.Effect;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.Ingredient;
import alchemy.srsys.object.Potion;

public class InventoryTest {

    private Inventory inventory;

    private IIngredient ingredient1;
    private IIngredient ingredient2;
    private IPotion potion1;

    @Before
    public void setUp() {
        inventory = new Inventory();

        // Mock ingredients and potion
        IEffect effectHealing = new Effect(1, "Healing","");
        IEffect effectPoison = new Effect(2, "Poison","");

        ingredient1 = new Ingredient(1, "Red Herb", Arrays.asList(effectHealing, null, null, null));
        ingredient2 = new Ingredient(2, "Blue Mushroom", Arrays.asList(effectPoison, null, null, null));

        potion1 = new Potion(1, "Healing Potion", Arrays.asList(effectHealing), ingredient1, ingredient2);
    }

    @Test
    public void testAddIngredient() {
        inventory.addIngredient(ingredient1, 3);

        assertTrue(inventory.containsIngredient(ingredient1));
        assertEquals(Integer.valueOf(3), inventory.getIngredients().get(ingredient1));
    }

    @Test
    public void testRemoveIngredient() {
        inventory.addIngredient(ingredient1, 3);
        inventory.removeIngredient(ingredient1, 2);

        assertTrue(inventory.containsIngredient(ingredient1));
        assertEquals(Integer.valueOf(1), inventory.getIngredients().get(ingredient1));

        // Remove the last one
        inventory.removeIngredient(ingredient1, 1);
        assertFalse(inventory.containsIngredient(ingredient1));
    }

    @Test
    public void testRemoveIngredient_NotInInventory() {
        // Attempt to remove ingredient not in inventory
        inventory.removeIngredient(ingredient1, 1);
        assertFalse(inventory.containsIngredient(ingredient1));
    }

    @Test
    public void testRemoveIngredient_ExceedingQuantity() {
        inventory.addIngredient(ingredient1, 2);

        // Remove more than available
        inventory.removeIngredient(ingredient1, 5);

        // Ingredient should be removed entirely
        assertFalse(inventory.containsIngredient(ingredient1));
    }

    @Test
    public void testAddPotion() {
        inventory.addPotion(potion1, 2);

        assertTrue(inventory.containsPotion(potion1));
        assertEquals(Integer.valueOf(2), inventory.getPotions().get(potion1));
    }

    @Test
    public void testRemovePotion() {
        inventory.addPotion(potion1, 2);
        inventory.removePotion(potion1, 1);

        assertTrue(inventory.containsPotion(potion1));
        assertEquals(Integer.valueOf(1), inventory.getPotions().get(potion1));

        // Remove the last one
        inventory.removePotion(potion1, 1);
        assertFalse(inventory.containsPotion(potion1));
    }

    @Test
    public void testRemovePotion_NotInInventory() {
        // Attempt to remove potion not in inventory
        inventory.removePotion(potion1, 1);
        assertFalse(inventory.containsPotion(potion1));
    }

    @Test
    public void testRemovePotion_ExceedingQuantity() {
        inventory.addPotion(potion1, 1);

        // Remove more than available
        inventory.removePotion(potion1, 3);

        // Potion should be removed entirely
        assertFalse(inventory.containsPotion(potion1));
    }
}
