package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IEffect;
import java.util.Map;

public interface IKnowledgeBook {
    /**
     * Updates the knowledge book with new information about an ingredient.
     * @param ingredient the ingredient to update
     */
    void updateIngredientKnowledge(IIngredient ingredient);

    /**
     * Retrieves the known information about all ingredients.
     * @return a map of ingredient names to their known effects
     */
    Map<String, IEffect[]> getKnownIngredients();

    /**
     * Checks if a specific effect of an ingredient is known.
     * @param ingredient the ingredient to check
     * @param effect the effect to check
     * @return true if the effect is known, false otherwise
     */
    boolean isEffectKnown(IIngredient ingredient, IEffect effect);
}
