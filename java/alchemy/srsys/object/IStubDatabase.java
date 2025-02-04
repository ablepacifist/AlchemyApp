package alchemy.srsys.object;

import alchemy.srsys.object.*;

import java.util.List;
import java.util.Map;

public interface IStubDatabase {
    // Master data methods
    void loadEffects(String filename);
    void loadIngredients(String filename);
    IIngredient findIngredientByName(String name);
    List<IIngredient> getAllIngredients();
    IEffect findEffectById(int id);
    List<IEffect> getAllEffects();

    // Player-specific data methods
    void loadPlayerInventory(String playerName, String filename);
    void loadKnowledgeBook(String playerName, String filename);

    // Inventory methods
    void addIngredientToInventory(String playerName, IIngredient ingredient);
    void removeIngredientFromInventory(String playerName, IIngredient ingredient);
    List<IIngredient> getInventoryIngredients(String playerName);

    // KnowledgeBook methods
    void updateKnowledgeBook(String playerName, IIngredient ingredient);
    Map<String, IEffect[]> getKnowledgeBook(String playerName);
}

