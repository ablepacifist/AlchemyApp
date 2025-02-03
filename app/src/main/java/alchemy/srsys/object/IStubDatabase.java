package alchemy.srsys.object;

import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import java.util.List;
import java.util.Map;

public interface IStubDatabase {
    /**
     * Loads ingredients from a text file.
     * @param filename the name of the file to load
     */
    void loadIngredients(String filename);

    /**
     * Loads effects from a text file.
     * @param filename the name of the file to load
     */
    void loadEffects(String filename);

    /**
     * Searches for an ingredient by name.
     * @param name the name of the ingredient
     * @return the ingredient if found, null otherwise
     */
    IIngredient findIngredientByName(String name);

    /**
     * Retrieves all ingredients.
     * @return a list of all ingredients
     */
    List<IIngredient> getAllIngredients();

    /**
     * Searches for an effect by ID.
     * @param id the ID of the effect
     * @return the effect if found, null otherwise
     */
    IEffect findEffectById(int id);

    /**
     * Retrieves all effects.
     * @return a list of all effects
     */
    List<IEffect> getAllEffects();

    void addIngredientToInventory(IIngredient ingredient);

    void removeIngredientFromInventory(IIngredient ingredient);

    List<IIngredient> getInventoryIngredients();

    void updateKnowledgeBook(IIngredient ingredient);

    Map<String, IEffect[]> getKnowledgeBook();
}
