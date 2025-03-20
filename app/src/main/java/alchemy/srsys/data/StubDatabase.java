package alchemy.srsys.data;

import java.util.*;
import alchemy.srsys.data.*;  // Adjust these imports to point to your actual classes
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

public class StubDatabase implements IStubDatabase {

    // MASTER DATA (effects and ingredients)
    private Map<Integer, IEffect> effectsById;
    private Map<String, IEffect> effectsByTitle;
    private Map<Integer, IIngredient> ingredientsById;
    private Map<String, IIngredient> ingredientsByName;
    private Map<Integer, IIngredient> ingredients;
    // PLAYER DATA
    private Map<Integer, Player> players;
    private int nextPotionId;
    private int nextPlayerId = 1;
    private static StubDatabase instance;

    // For each player, store the KnowledgeBook as a Map: ingredientId -> List of learned effects
    private Map<Integer, Map<Integer, List<IEffect>>> playerKnowledgeBooks;

    public StubDatabase() {
        effectsById = new HashMap<>();
        effectsByTitle = new HashMap<>();
        ingredientsById = new HashMap<>();
        ingredientsByName = new HashMap<>();
        players = new HashMap<>();
        nextPotionId = 1;
        playerKnowledgeBooks = new HashMap<>();
        ingredients = new HashMap<>();
        // Initialize master data and sample players
        initializeEffects();
        initializeIngredients();
        initializePlayers();
        ingredients.put(1, new Ingredient(1, "Flour", new ArrayList<>()));
        ingredients.put(2, new Ingredient(2, "Water", new ArrayList<>()));
        ingredients.put(3, new Ingredient(3, "Sugar", new ArrayList<>()));
    }
    public static synchronized StubDatabase getInstance(){
        if (instance == null) {
            instance = new StubDatabase();
        }
        return instance;
    }
    // ------------------------------
    // MASTER DATA INITIALIZATION
    // ------------------------------

    private void initializeEffects() {
        // Initialize effects with correct constructor
        addEffect(new Effect(1, "Healing", "Restores health over time."));
        addEffect(new Effect(2, "Poison", "Deals damage over time."));
        addEffect(new Effect(3, "Strength", "Increases physical strength."));
        addEffect(new Effect(4, "Weakness", "Reduces resistance to attacks."));
        IEffect spark = new Effect(5, "Spark", "A shared magical effect");
        addEffect(spark);
    }


    public void addEffect(IEffect effect) {
        effectsById.put(effect.getId(), effect);
        effectsByTitle.put(effect.getTitle().toLowerCase(), effect);
    }

    private void initializeIngredients() {
        // Example ingredients. Note: null values in the effects list are ignored.
        addIngredient(new Ingredient(1, "Red Herb", Arrays.asList(
                effectsById.get(1), // Healing
                effectsById.get(3),  // Strength
                effectsById.get(5)  // Spark
        )));
        addIngredient(new Ingredient(2, "Blue Mushroom", Arrays.asList(
                effectsById.get(2), // Poison
                effectsById.get(4),  // Weakness
                effectsById.get(5)  // Spark
        )));
        addIngredient(new Ingredient(3, "Yellow Flower", Arrays.asList(
                effectsById.get(1), // Healing
                effectsById.get(2),  // Poison
                effectsById.get(5)  // Spark
        )));
    }

    public void addIngredient(IIngredient ingredient) {
        ingredientsById.put(ingredient.getId(), ingredient);
        ingredientsByName.put(ingredient.getName().toLowerCase(), ingredient);
    }

    private void initializePlayers() {
        // Create a sample player with an initial inventory
        IInventory inv = new Inventory();
        IIngredient redHerb = getIngredientByName("Red Herb");
        IIngredient blueMushroom = getIngredientByName("Blue Mushroom");
        IIngredient yellowFlower = getIngredientByName("Yellow Flower");
        inv.addIngredient(redHerb, 3);
        inv.addIngredient(blueMushroom, 1);
        inv.addIngredient(yellowFlower, 1);

        // The player's knowledge book starts empty
        Player player = new Player(1, "alex", "zx7364pl", inv, new KnowledgeBook(new HashMap<>()));
        players.put(player.getId(), player);

        // Update nextPlayerId to start from the next available ID
        nextPlayerId = 2;
    }


    // ------------------------------
    // IStubDatabase IMPLEMENTATION
    // ------------------------------

    @Override
    public int getNextPotionId() {
        return nextPotionId++;
    }

    @Override
    public int getNextPlayerId() {
        this.nextPlayerId =nextPlayerId+1;
        return nextPlayerId;
    }

    @Override
    public IIngredient getIngredientByName(String name) {
        return ingredientsByName.get(name.toLowerCase());
    }

    @Override
    public List<IIngredient> getAllIngredients() {
        return new ArrayList<>(ingredientsById.values());
    }

    @Override
    public void addPlayer(Player player) {
        if (player != null) {
            players.put(player.getId(), player);
            if (!playerKnowledgeBooks.containsKey(player.getId())) {
                playerKnowledgeBooks.put(player.getId(), new HashMap<>());
            }
        }
    }


    @Override
    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    @Override
    public Player getPlayerByUsername(String username) {
        for (Player p : players.values()) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    @Override
    public void addPotionToPlayerInventory(int playerId, Potion potion, int quantity) {
        Player player = players.get(playerId);
        if (player != null) {
            player.getInventory().addPotion(potion, quantity);
        }
    }

    @Override
    public void addIngredientToPlayerInventory(int playerId, IIngredient ingredient, int quantity) {
        Player player = players.get(playerId);
        if (player != null) {
            player.getInventory().addIngredient(ingredient, quantity);
        }
    }

    @Override
    public void removeIngredientFromPlayerInventory(int playerId, IIngredient ingredient, int quantity) {
        Player player = players.get(playerId);
        if (player != null) {
            player.getInventory().removeIngredient(ingredient, quantity);
        }
    }

    @Override
    public IInventory getPlayerInventory(int playerId) {
        Player player = players.get(playerId);
        return (player != null) ? player.getInventory() : null;
    }

    @Override
    public void addKnowledgeEntry(int playerId, IIngredient ingredient, IEffect effect) {
        Map<Integer, List<IEffect>> kb = playerKnowledgeBooks.get(playerId);
        if (kb == null) {
            kb = new HashMap<>();
            playerKnowledgeBooks.put(playerId, kb);
        }
        int ingId = ingredient.getId();
        List<IEffect> learnedEffects = kb.get(ingId);
        if (learnedEffects == null) {
            learnedEffects = new ArrayList<>();
            kb.put(ingId, learnedEffects);
        }
        if (!learnedEffects.contains(effect)) {
            learnedEffects.add(effect);
        }
    }

    @Override
    public void updateKnowledgeBook(int playerId, IIngredient ingredient) {
        // For this stub, simply copy the ingredient's current (non-null) effects into the player's knowledge book.
        Map<Integer, List<IEffect>> kb = playerKnowledgeBooks.get(playerId);
        if (kb == null) {
            kb = new HashMap<>();
            playerKnowledgeBooks.put(playerId, kb);
        }
        int ingId = ingredient.getId();
        List<IEffect> effects = new ArrayList<>();
        for (IEffect e : ingredient.getEffects()) {
            if (e != null) {
                effects.add(e);
            }
        }
        kb.put(ingId, effects);
    }

    @Override
    public IKnowledgeBook getKnowledgeBook(int playerId) {
        Map<Integer, List<IEffect>> kb = playerKnowledgeBooks.get(playerId);
        if (kb == null) {
            // Return an empty KnowledgeBook instead of an empty map.
            return new KnowledgeBook(new HashMap<>());
        }
        // Create a defensive copy of the knowledge map.
        Map<Integer, List<IEffect>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<IEffect>> entry : kb.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        // Wrap the copy in a KnowledgeBook instance.
        return new KnowledgeBook(copy);
    }


    @Override
    public List<IEffect> getEffectsForIngredient(int ingredientId) {
        IIngredient ingredient = ingredientsById.get(ingredientId);
        if (ingredient != null) {
            List<IEffect> effects = new ArrayList<>();
            for (IEffect e : ingredient.getEffects()) {
                if (e != null) {
                    effects.add(e);
                }
            }
            return effects;
        }
        return new ArrayList<>();
    }
    @Override
    public void removePotionFromPlayerInventory(int playerId, Potion potion, int quantity) {
        Player player = players.get(playerId);
        if (player != null) {
            // Assuming your Inventory class has a method to remove a potion:
            player.getInventory().removePotion(potion, quantity);
        }
    }

    @Override
    public IIngredient getIngredientById(int ingredientId) {
        // Check if the ingredient exists in the map.
        IIngredient ingredient = ingredients.get(ingredientId);
        if (ingredient == null) {
            System.out.println("DEBUG: Ingredient with ID " + ingredientId + " not found.");
        }
        return ingredient;
    }


    @Override
    public void addPotion(Potion potion) {
        // only needed for HSQLDB
    }


}
