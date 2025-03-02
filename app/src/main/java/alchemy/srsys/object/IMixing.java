package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IPotion;

public interface IMixing {
    /**
     * Mixes two ingredients to create a potion.
     * Updates player's inventory and knowledge book.
     * @param player the player
     * @param ingredient1 first ingredient
     * @param ingredient2 second ingredient
     * @return the created potion if successful, null otherwise
     */
    IPotion mixIngredients(Player player, IIngredient ingredient1, IIngredient ingredient2);
}
