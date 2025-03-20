package alchemy.srsys.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.KnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;

public class PlayerManager {
    private final IStubDatabase db;

    public PlayerManager(IStubDatabase db) {
        this.db = db;
    }
    public Player loginPlayer(String username, String password) {
        Player player = db.getPlayerByUsername(username);
        if (player == null) {
            throw new IllegalArgumentException("Invalid username. No such player exists.");
        }

        if (!player.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password. Please try again.");
        }

        return player;
    }

    public void registerPlayer(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if a player with this username already exists.
        Player existingPlayer = db.getPlayerByUsername(username);
        if (existingPlayer != null) {
            throw new IllegalArgumentException("Username already taken. Please choose a different username.");
        }

        // Create the player.
        createPlayer(username, password);
    }

    /**
     * Create a new player with an empty inventory and knowledge book.
     */
    public void createPlayer(String username, String password) {
        // Let the database generate the next player ID.
        int playerId = db.getNextPlayerId();
        Inventory inventory = new Inventory(); // Assuming a concrete Inventory class.
        IKnowledgeBook knowledgeBook = new KnowledgeBook(new java.util.HashMap<>());
        Player newPlayer = new Player(playerId, username, password, inventory, knowledgeBook);
        db.addPlayer(newPlayer);
        System.out.println("Player created: " + newPlayer);
    }

    public Player getPlayerById(int playerId) {
        return db.getPlayer(playerId);
    }

    public Player getPlayerByUsername(String username) {
        return db.getPlayerByUsername(username);
    }

    /**
     * Get the player's inventory.
     */
    public alchemy.srsys.object.IInventory getInventory(int playerId) {
        return db.getPlayerInventory(playerId);
    }
    public IKnowledgeBook getKnowledgeBook(int playerId) {
        return db.getKnowledgeBook(playerId);
    }

    /**
     * Forage: Randomly selects an ingredient from the master list and adds one unit
     * of it to the player's inventory.
     *
     * @param playerId the ID of the player for whom to forage
     * @return the name of the foraged ingredient, or an empty string if none available.
     */
    public String forage(int playerId) {
        List<IIngredient> masterIngredients = db.getAllIngredients();
        if (masterIngredients.isEmpty()) {
            System.out.println("No ingredients available to forage.");
            return "";
        }
        Random rand = new Random();
        IIngredient randomIngredient = masterIngredients.get(rand.nextInt(masterIngredients.size()));
        db.addIngredientToPlayerInventory(playerId, randomIngredient, 1);
        System.out.println("Foraged " + randomIngredient.getName() + " for player " + playerId);
        return randomIngredient.getName();
    }
    /**
     * Consumes one unit of the specified ingredient from the player's inventory.
     * After consumption, if there is an effect on that ingredient that the player hasn't learned yet,
     * that effect is added to the player's KnowledgeBook.
     *
     * @param playerId   the ID of the player
     * @param ingredient the ingredient to consume
     */
    public void consumeIngredient(int playerId, IIngredient ingredient) {
        // Remove one unit of the ingredient.
        db.removeIngredientFromPlayerInventory(playerId, ingredient, 1);
        System.out.println("Consumed one unit of " + ingredient.getName() + " for player " + playerId);

        // Retrieve master effects for the given ingredient.
        List<IEffect> masterEffects = db.getEffectsForIngredient(ingredient.getId());

        // Retrieve the player's current knowledge from the knowledge book.
        IKnowledgeBook kb = db.getKnowledgeBook(playerId);

        // Look for the first effect that is not already known.
        IEffect effectToLearn = null;
        for (IEffect effect : masterEffects) {
            if (!kb.hasKnowledge(ingredient, effect)) {
                effectToLearn = effect;
                break;
            }
        }

        if (effectToLearn != null) {
            // Use the public API on KnowledgeBook to add the new effect.
            kb.addKnowledge(ingredient, effectToLearn);
            // Also update through the database if needed.
            db.addKnowledgeEntry(playerId, ingredient, effectToLearn);
            System.out.println("Player " + playerId + " learned new effect: " + effectToLearn.getTitle());
        } else {
            System.out.println("Player " + playerId + " already knows all effects for " + ingredient.getName());
        }
    }


    /**
     * Consumes one unit of the specified potion from the player's inventory.
     * Consuming a potion might, for example, apply the potion's effect to the player.
     * Here, we'll simply remove one unit from inventory.
     *
     * @param playerId the ID of the player
     * @param potion   the potion to consume
     */
    public void consumePotion(int playerId, IPotion potion) {
        // Remove one unit of the potion from the player's inventory.
        db.removePotionFromPlayerInventory(playerId, (Potion) potion, 1);
        System.out.println("Player " + playerId + " consumed potion: " + potion.getName());

        // Optional: trigger additional logic here (e.g., apply the potion's effects)
    }
}
