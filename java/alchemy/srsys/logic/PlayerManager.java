package alchemy.srsys.logic;

import android.content.Context;

import alchemy.srsys.object.*;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.data.StubDatabase;

import java.util.List;
import java.util.Map;

/**
 * The PlayerManager class manages player interactions and data.
 * It acts as a mediator between the UI layer and the data layer,
 * handling operations related to the player's inventory,
 * knowledge book, mixing, and consumption of ingredients.
 */
public class PlayerManager implements IPlayerManager {

    private Player player;
    private IStubDatabase database;
    private IInventory inventory;
    private IKnowledgeBook knowledgeBook;
    private IMixing mixing;


    /**
     * Constructs a PlayerManager instance for a specific player.
     *
     * @param player The Player object representing the current player.
     */
    public PlayerManager(Player player,Context context) {
        this.player = player;
        this.database = new StubDatabase(context);

        // Load master data
        database.loadEffects("effects.txt");
        database.loadIngredients("masterList.txt");

        // Load player-specific data
        database.loadPlayerInventory(player.getName(), "inventory.txt");
        database.loadKnowledgeBook(player.getName(), "ingredientList.txt");

        // Initialize inventory and knowledge book
        this.inventory = new Inventory(database, player);
        this.knowledgeBook = new KnowledgeBook(database, player);

        // Initialize mixing and consumption handlers
        this.mixing = new Mixing();

    }

    // Inventory methods

    /**
     * Adds an ingredient to the player's inventory.
     *
     * @param ingredient The ingredient to add.
     */
    @Override
    public void addIngredientToInventory(IIngredient ingredient) {
        inventory.addIngredient(ingredient);
    }

    /**
     * Removes an ingredient from the player's inventory.
     *
     * @param ingredient The ingredient to remove.
     */
    @Override
    public void removeIngredientFromInventory(IIngredient ingredient) {
        inventory.removeIngredient(ingredient);
    }

    /**
     * Retrieves the player's current inventory.
     *
     * @return A List of IIngredient objects.
     */
    @Override
    public List<IIngredient> getInventory() {
        return inventory.getIngredients();
    }

    // KnowledgeBook methods

    /**
     * Updates the player's knowledge book with a new ingredient.
     *
     * @param ingredient The ingredient whose effects are to be updated.
     */
    @Override
    public void updateKnowledgeBook(IIngredient ingredient) {
        knowledgeBook.updateIngredientKnowledge(ingredient);
    }

    /**
     * Retrieves the player's known ingredients and their effects.
     *
     * @return A Map of ingredient names to arrays of IEffect objects.
     */
    @Override
    public Map<String, IEffect[]> getKnownIngredients() {
        return knowledgeBook.getKnownIngredients();
    }

    /**
     * Checks if a specific effect of an ingredient is known to the player.
     *
     * @param ingredient The ingredient to check.
     * @param effect     The effect to check for.
     * @return True if the effect is known, false otherwise.
     */
    @Override
    public boolean isEffectKnown(IIngredient ingredient, IEffect effect) {
        return knowledgeBook.isEffectKnown(ingredient, effect);
    }

    // Mixing methods

    /**
     * Attempts to mix two ingredients to create a potion.
     * Updates the knowledge book and removes ingredients from inventory if successful.
     *
     * @param ingredient1 The first ingredient.
     * @param ingredient2 The second ingredient.
     * @return An IPotion object if successful, null otherwise.
     */
    @Override
    public IPotion mixIngredients(IIngredient ingredient1, IIngredient ingredient2) {
        IPotion potion = mixing.mixIngredients(ingredient1, ingredient2);
        if (potion != null) {
            // Update knowledge book with new effects learned
            updateKnowledgeBook(ingredient1);
            updateKnowledgeBook(ingredient2);

            // Remove used ingredients from inventory
            removeIngredientFromInventory(ingredient1);
            removeIngredientFromInventory(ingredient2);

            // Optionally, add the potion to a potions inventory or handle it accordingly
            // e.g., addPotionToInventory(potion);

            return potion;
        }
        // Mixing failed
        return null;
    }

    // Consumption methods

    /**
     * Allows the player to consume an ingredient, learning a new effect.
     * Removes the ingredient from inventory after consumption.
     *
     * @param ingredient The ingredient to consume.
     */
    @Override
    public void consumeIngredient(IIngredient ingredient) {
        //consumption.eatIngredient(ingredient, knowledgeBook);
        removeIngredientFromInventory(ingredient);
    }

    // Additional utility methods

    /**
     * Retrieves the player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Saves the player's data to the data layer.
     * This method can be called when the player exits the game to persist data.
     */
    public void savePlayerData() {
        // Implement saving logic if required
        // E.g., database.savePlayerInventory(player.getName());
        //       database.saveKnowledgeBook(player.getName());
    }
}
