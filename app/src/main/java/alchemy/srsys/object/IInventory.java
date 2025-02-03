package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import java.util.List;

public interface IInventory {
    /**
     * Adds an ingredient to the inventory.
     * @param ingredient the ingredient to add
     */
    void addIngredient(IIngredient ingredient);

    /**
     * Removes an ingredient from the inventory.
     * @param ingredient the ingredient to remove
     */
    void removeIngredient(IIngredient ingredient);

    /**
     * Gets the list of ingredients in the inventory.
     * @return a list of ingredients
     */
    List<IIngredient> getIngredients();

    /**
     * Checks if the inventory contains a specific ingredient.
     * @param ingredient the ingredient to check
     * @return true if present, false otherwise
     */
    boolean contains(IIngredient ingredient);
}
