package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IPotion;

public interface IMixing {
    /**
     * Attempts to mix two ingredients to create a potion.
     * If the ingredients share common effects, a potion is created.
     * @param ingredient1 the first ingredient
     * @param ingredient2 the second ingredient
     * @return the resulting potion if successful, null otherwise
     */
    IPotion mixIngredients(IIngredient ingredient1, IIngredient ingredient2);
}
