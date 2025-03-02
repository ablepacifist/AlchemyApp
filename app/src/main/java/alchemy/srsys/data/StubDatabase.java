package alchemy.srsys.data;

import alchemy.srsys.object.*;
import java.util.*;


public class StubDatabase implements IStubDatabase {
    // Master data
    private Map<Integer, IEffect> effectsById;
    private Map<String, IEffect> effectsByTitle;
    private Map<Integer, IIngredient> ingredientsById;
    private Map<String, IIngredient> ingredientsByName;

    // Player data
    private Map<Integer, Player> players;
    private int nextPotionId;
    private int nextPlayerId=1;

    // Inventory and Knowledge Book
    private List<IIngredient> inventory;
    private Map<String, IEffect[]> knowledgeBook;

    public StubDatabase() {
        effectsById = new HashMap<>();
        effectsByTitle = new HashMap<>();
        ingredientsById = new HashMap<>();
        ingredientsByName = new HashMap<>();
        players = new HashMap<>();
        nextPotionId = 1;

        inventory = new ArrayList<>();
        knowledgeBook = new HashMap<>();

        initializeEffects();
        initializeIngredients();
        initializePlayers();
    }

    // Initialize master effects
    private void initializeEffects() {
        // Example effects
        addEffect(new Effect(1, "Healing"));
        addEffect(new Effect(2, "Poison"));
        addEffect(new Effect(3, "Strength"));
        addEffect(new Effect(4, "Weakness"));
        // Add more effects as needed
    }

    public void addEffect(IEffect effect) {
        effectsById.put(effect.getId(), effect);
        effectsByTitle.put(effect.getTitle().toLowerCase(), effect);
    }

    // Initialize master ingredients
    private void initializeIngredients() {
        // Example ingredients
        addIngredient(new Ingredient(1, "Red Herb", Arrays.asList(
                effectsById.get(1), // Healing
                effectsById.get(3), // Strength
                null,
                null
        )));
        addIngredient(new Ingredient(2, "Blue Mushroom", Arrays.asList(
                effectsById.get(2), // Poison
                effectsById.get(4), // Weakness
                null,
                null
        )));
        addIngredient(new Ingredient(3, "Yellow Flower", Arrays.asList(
                effectsById.get(1), // Healing
                effectsById.get(2), // Poison
                null,
                null
        )));
        // Add more ingredients as needed
    }
    // Initialize players
    private void initializePlayers() {
        // Create a player with username "alex" and password "zx7364pl" and ID 1
        Player player = new Player(1, "alex", "zx7364pl", getAllIngredients());

        // Add some inventory items to the player
        IInventory inventory = player.getInventory();

        IIngredient redHerb = getIngredientByName("Red Herb");
        IIngredient blueMushroom = getIngredientByName("Blue Mushroom");
        IIngredient yellowFlower = getIngredientByName("Yellow Flower");
        // Add quantities of ingredients
        inventory.addIngredient(redHerb, 3);       // Red Herb x3
        inventory.addIngredient(blueMushroom, 1);  // Blue Mushroom x1
        inventory.addIngredient(yellowFlower, 1);  // yellow flower x4

        // Add the player to the players map
        players.put(player.getId(), player);
    }

    public void addIngredient(IIngredient ingredient) {
        ingredientsById.put(ingredient.getId(), ingredient);
        ingredientsByName.put(ingredient.getName().toLowerCase(), ingredient);
    }

    // Player management
    public void addPlayer(Player player) {
        players.put(player.getId(), player);
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    // Ingredient retrieval
    public IIngredient getIngredientById(int id) {
        return ingredientsById.get(id);
    }

    public IIngredient getIngredientByName(String name) {
        return ingredientsByName.get(name.toLowerCase());
    }

    @Override
    public void loadIngredients(String filename) {
        // Simulate loading ingredients from a file
        // For stub purposes, we'll add a new ingredient
        addIngredient(new Ingredient(4, "Black Root", Arrays.asList(
                effectsById.get(3), // Strength
                effectsById.get(4), // Weakness
                null,
                null
        )));
    }

    @Override
    public void loadEffects(String filename) {
        // Simulate loading effects from a file
        // For stub purposes, we'll add a new effect
        addEffect(new Effect(5, "Invisibility"));
    }

    @Override
    public IIngredient findIngredientByName(String name) {
        return ingredientsByName.get(name.toLowerCase());
    }

    @Override
    public IEffect findEffectById(int id) {
        return effectsById.get(id);
    }

    public List<IIngredient> getAllIngredients() {
        return new ArrayList<>(ingredientsById.values());
    }

    public List<IEffect> getAllEffects() {
        return new ArrayList<>(effectsById.values());
    }

    // Effect retrieval
    public IEffect getEffectById(int id) {
        return effectsById.get(id);
    }

    public IEffect getEffectByTitle(String title) {
        return effectsByTitle.get(title.toLowerCase());
    }

    // Inventory management
    @Override
    public void addIngredientToInventory(IIngredient ingredient) {
        inventory.add(ingredient);
    }

    @Override
    public void removeIngredientFromInventory(IIngredient ingredient) {
        inventory.remove(ingredient);
    }

    @Override
    public List<IIngredient> getInventoryIngredients() {
        return new ArrayList<>(inventory);
    }

    // Knowledge Book management
    @Override
    public void updateKnowledgeBook(IIngredient ingredient) {
        knowledgeBook.put(ingredient.getName(), ingredient.getEffects().toArray(new IEffect[0]));
    }

    @Override
    public Map<String, IEffect[]> getKnowledgeBook() {
        return knowledgeBook;
    }

    public int getNextPlayerId() {
        return nextPlayerId++;
    }

    public Player getPlayerByUsername(String username) {
        for (Player player : players.values()) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                return player;
            }
        }
        return null;
    }

    // Potion creation
    public int getNextPotionId() {
        return nextPotionId++;
    }

}
