package alchemy.srsys.data;

import android.content.Context;
import android.content.res.AssetManager;

import alchemy.srsys.object.*;

import java.io.*;
import java.util.*;

public class StubDatabase implements IStubDatabase {
    private Map<Integer, IEffect> effectsById;
    private Map<String, IEffect> effectsByName;
    private Map<String, IIngredient> ingredientsByName;

    private Map<String, List<IIngredient>> playerInventories;
    private Map<String, Map<String, IEffect[]>> playerKnowledgeBooks;

    private AssetManager assetManager;

    public StubDatabase(Context context) {
        this.assetManager = context.getAssets();
        effectsById = new HashMap<>();
        effectsByName = new HashMap<>();
        ingredientsByName = new HashMap<>();
        playerInventories = new HashMap<>();
        playerKnowledgeBooks = new HashMap<>();
    }

    private BufferedReader getFileReader(String filename) throws IOException {
        InputStream inputStream = assetManager.open(filename);
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void loadEffects(String filename) {
        try (BufferedReader reader = getFileReader(filename)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    IEffect effect = new Effect(id, name);
                    effectsById.put(id, effect);
                    effectsByName.put(name.toLowerCase(), effect);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading effects: " + e.getMessage());
        }
    }

    @Override
    public void loadIngredients(String filename) {
        try (BufferedReader reader = getFileReader(filename)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String name = parts[0].trim();
                    IIngredient ingredient = new Ingredient(name);
                    for (int i = 1; i < parts.length && i <= 4; i++) {
                        String effectName = parts[i].trim();
                        if (!effectName.equalsIgnoreCase("NULL") && !effectName.isEmpty()) {
                            IEffect effect = findEffectByName(effectName);
                            if (effect != null) {
                                ingredient.learnEffect(effect);
                            } else {
                                System.err.println("Effect not found: " + effectName);
                            }
                        }
                    }
                    ingredientsByName.put(name.toLowerCase(), ingredient);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading ingredients: " + e.getMessage());
        }
    }

    @Override
    public void loadPlayerInventory(String playerName, String filename) {
        List<IIngredient> inventory = new ArrayList<>();
        try (BufferedReader reader = getFileReader(filename)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String itemType = parts[0].trim();
                    String name = parts[1].trim();
                    String quantityStr = parts[2].trim();
                    if (!quantityStr.equalsIgnoreCase("NULL") && !quantityStr.isEmpty()) {
                        int quantity = Integer.parseInt(quantityStr);
                        if (itemType.equalsIgnoreCase("Ingredient")) {
                            IIngredient ingredient = findIngredientByName(name);
                            if (ingredient != null) {
                                for (int i = 0; i < quantity; i++) {
                                    inventory.add(ingredient);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading player inventory: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing quantity: " + e.getMessage());
        }
        playerInventories.put(playerName, inventory);
    }

    @Override
    public void loadKnowledgeBook(String playerName, String filename) {
        Map<String, IEffect[]> knowledgeBook = new HashMap<>();
        try (BufferedReader reader = getFileReader(filename)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    String ingredientName = parts[0].trim();
                    IEffect[] knownEffects = new IEffect[4];
                    for (int i = 1; i < parts.length && i <= 4; i++) {
                        String effectName = parts[i].trim();
                        if (!effectName.equalsIgnoreCase("NULL") && !effectName.isEmpty()) {
                            IEffect effect = findEffectByName(effectName);
                            if (effect != null) {
                                knownEffects[i - 1] = effect;
                            }
                        }
                    }
                    knowledgeBook.put(ingredientName, knownEffects);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading knowledge book: " + e.getMessage());
        }
        playerKnowledgeBooks.put(playerName, knowledgeBook);
    }

    @Override
    public IIngredient findIngredientByName(String name) {
        return ingredientsByName.get(name.toLowerCase());
    }

    @Override
    public List<IIngredient> getAllIngredients() {
        return new ArrayList<>(ingredientsByName.values());
    }

    @Override
    public IEffect findEffectById(int id) {
        return effectsById.get(id);
    }

    @Override
    public List<IEffect> getAllEffects() {
        return new ArrayList<>(effectsById.values());
    }

    @Override
    public void addIngredientToInventory(String playerName, IIngredient ingredient) {
        playerInventories.computeIfAbsent(playerName, k -> new ArrayList<>()).add(ingredient);
    }

    @Override
    public void removeIngredientFromInventory(String playerName, IIngredient ingredient) {
        List<IIngredient> inventory = playerInventories.get(playerName);
        if (inventory != null) {
            inventory.remove(ingredient);
        }
    }

    @Override
    public List<IIngredient> getInventoryIngredients(String playerName) {
        return playerInventories.getOrDefault(playerName, new ArrayList<>());
    }

    @Override
    public void updateKnowledgeBook(String playerName, IIngredient ingredient) {
        Map<String, IEffect[]> knowledgeBook = playerKnowledgeBooks.computeIfAbsent(playerName, k -> new HashMap<>());
        IEffect[] knownEffects = knowledgeBook.getOrDefault(ingredient.getName(), new IEffect[4]);
        IEffect[] ingredientEffects = ingredient.getEffects();
        for (int i = 0; i < knownEffects.length; i++) {
            if (knownEffects[i] == null && ingredientEffects[i] != null) {
                knownEffects[i] = ingredientEffects[i];
            }
        }
        knowledgeBook.put(ingredient.getName(), knownEffects);
    }

    @Override
    public Map<String, IEffect[]> getKnowledgeBook(String playerName) {
        return playerKnowledgeBooks.getOrDefault(playerName, new HashMap<>());
    }

    private IEffect findEffectByName(String name) {
        return effectsByName.get(name.toLowerCase());
    }
}
