package alchemy.srsys.object;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IEffect;
public interface IPotion {
    /**
     * Gets the name of the potion.
     * @return the potion name
     */
    String getName();

    /**
     * Gets the array of effects of the potion.
     * @return an array of effects
     */
    IEffect[] getEffects();

    /**
     * Gets the first ingredient used in the potion.
     * @return the first ingredient
     */
    IIngredient getIngredient1();

    /**
     * Gets the second ingredient used in the potion.
     * @return the second ingredient
     */
    IIngredient getIngredient2();
}
