package alchemy.srsys.logic;

import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private IStubDatabase database;

    public GameManager(IStubDatabase database) {
        this.database = database;
    }

    public Player getPlayerById(int playerId) {
        return database.getPlayer(playerId);
    }

    public Map<String, List<IEffect>> getPlayerKnowledgeBookEntries(Player player) {
        // Retrieve all ingredients
        List<IIngredient> allIngredients = database.getAllIngredients();

        Map<String, List<IEffect>> knowledgeEntries = new HashMap<>();

        for (IIngredient ingredient : allIngredients) {
            String ingredientName = ingredient.getName();

            // Get known effects for this ingredient from the player's knowledge book
            List<IEffect> knownEffects = player.getKnowledgeBook().getKnownEffects(ingredient);

            knowledgeEntries.put(ingredientName, knownEffects);
        }

        return knowledgeEntries;
    }
    public IIngredient getIngredientByName(String name) {
        return database.getIngredientByName(name);
    }

    public IPotion mixIngredients(Player player, String ingredientName1, String ingredientName2) {
        IIngredient ingredient1 = getIngredientByName(ingredientName1);
        IIngredient ingredient2 = getIngredientByName(ingredientName2);

        if (ingredient1 == null || ingredient2 == null) {
            // One or both ingredients not found
            return null;
        }

        if (ingredient1.equals(ingredient2)) {
            // Same ingredients selected
            return null;
        }

        // Create Mixing instance
        Mixing mixing = new Mixing(database);
        IPotion potion = mixing.mixIngredients(player, ingredient1, ingredient2);

        return potion;
    }
    public void consumeIngredient(Player player, IIngredient ingredient) {
        IInventory inventory = player.getInventory();
        inventory.removeIngredient(ingredient, 1);

        // Learn a new effect if any unknown effects exist
        IKnowledgeBook knowledgeBook = player.getKnowledgeBook();
        List<IEffect> knownEffects = knowledgeBook.getKnownEffects(ingredient);
        List<IEffect> allEffects = ingredient.getEffects();

        for (IEffect effect : allEffects) {
            if (effect != null && !knownEffects.contains(effect)) {
                knowledgeBook.learnEffect(ingredient, effect);
                break;
            }
        }
    }

    public void consumePotion(Player player, IPotion potion) {
        IInventory inventory = player.getInventory();
        inventory.removePotion(potion, 1);
    }

}
