package alchemy.srsys.logic;

import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.Player;

import java.util.List;

/**
 * The Inventory class manages the player's inventory of ingredients.
 * It interacts with the StubDatabase to retrieve and update inventory data.
 */
public class Inventory implements IInventory {
    private IStubDatabase database;
    private Player player;

    /**
     * Constructs an Inventory instance for a specific player.
     *
     * @param database The StubDatabase instance used for data operations.
     * @param player   The Player whose inventory is being managed.
     */
    public Inventory(IStubDatabase database, Player player) {
        this.database = database;
        this.player = player;
    }

    /**
     * Adds an ingredient to the player's inventory.
     *
     * @param ingredient The ingredient to add.
     */
    @Override
    public void addIngredient(IIngredient ingredient) {
        if (ingredient != null) {
            database.addIngredientToInventory(player.getName(), ingredient);
        }
    }

    /**
     * Removes an ingredient from the player's inventory.
     *
     * @param ingredient The ingredient to remove.
     */
    @Override
    public void removeIngredient(IIngredient ingredient) {
        if (ingredient != null) {
            database.removeIngredientFromInventory(player.getName(), ingredient);
        }
    }

    /**
     * Retrieves the list of ingredients in the player's inventory.
     *
     * @return A List of IIngredient objects representing the player's inventory.
     */
    @Override
    public List<IIngredient> getIngredients() {
        return database.getInventoryIngredients(player.getName());
    }

    /**
     * Checks if the player's inventory contains a specific ingredient.
     *
     * @param ingredient The ingredient to check for.
     * @return True if the inventory contains the ingredient, false otherwise.
     */
    @Override
    public boolean contains(IIngredient ingredient) {
        return getIngredients().contains(ingredient);
    }
}
