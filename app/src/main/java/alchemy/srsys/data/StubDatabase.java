package alchemy.srsys.data;

import alchemy.srsys.object.*;
import java.io.*;
import java.util.*;


import alchemy.srsys.object.*;

import java.io.*;
import java.util.*;

public class StubDatabase implements IStubDatabase {
    // Master data
    private Map<Integer, IEffect> effectsById;
    private Map<String, IEffect> effectsByName;
    private Map<String, IIngredient> ingredientsByName;

    // Player-specific data
    private List<IIngredient> inventory;
    private Map<String, IEffect[]> knowledgeBook;

    public StubDatabase() {
        effectsById = new HashMap<>();
        effectsByName = new HashMap<>();
        ingredientsByName = new HashMap<>();
        inventory = new ArrayList<>();
        knowledgeBook = new HashMap<>();
        loadEffects("effects.txt");
        loadKnowledgeBook("masterList.txt");
        loadIngredients("ingredientList.txt");
        loadPlayerInventory("inventory.txt");
    }

    // Methods to load master data from files (effects.txt, masterList.txt)
    // ...

    // Methods to load player data from files (inventory.txt, ingredientList.txt)
    public void loadPlayerInventory(String filename) {
        // Load inventory from inventory.txt
        // Format: ItemType,Name,Quantity
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String itemType = parts[0].trim();
                    String name = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2].trim());

                    if (itemType.equalsIgnoreCase("Ingredient")) {
                        IIngredient ingredient = findIngredientByName(name);
                        if (ingredient != null) {
                            for (int i = 0; i < quantity; i++) {
                                inventory.add(ingredient);
                            }
                        }
                    }
                    // Handle potions if needed
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading player inventory: " + e.getMessage());
        }
    }

    public void loadKnowledgeBook(String filename) {
        // Load knowledge from ingredientList.txt
        // Format: IngredientName,Effect1,Effect2,Effect3,Effect4
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
    }

    // Methods to manage inventory
    public void addIngredientToInventory(String playerName,IIngredient ingredient) {
        playerInventories.computeIfAbsent(playerName, k -> new ArrayList<>()).add(ingredient);
    }

    public void removeIngredientFromInventory(IIngredient ingredient) {
        inventory.remove(ingredient);
    }

    public List<IIngredient> getInventoryIngredients() {
        return new ArrayList<>(inventory);
    }

    // Methods to manage knowledge book
    public void updateKnowledgeBook(IIngredient ingredient) {
        String name = ingredient.getName();
        IEffect[] knownEffects = knowledgeBook.getOrDefault(name, new IEffect[4]);
        IEffect[] ingredientEffects = ingredient.getEffects();

        for (int i = 0; i < knownEffects.length; i++) {
            if (knownEffects[i] == null && ingredientEffects[i] != null) {
                knownEffects[i] = ingredientEffects[i];
            }
        }
        knowledgeBook.put(name, knownEffects);
    }

    public Map<String, IEffect[]> getKnowledgeBook() {
        return knowledgeBook;
    }

    // Helper methods


    @Override
    public void loadEffects(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: id,effectName
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
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Format: ingredientName,effect1,effect2,effect3,effect4
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

    private IEffect findEffectByName(String name) {
        return effectsByName.get(name.toLowerCase());
    }
}
