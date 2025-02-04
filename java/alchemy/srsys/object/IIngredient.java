package alchemy.srsys.object;
import alchemy.srsys.object.IEffect;

public interface IIngredient {
    /**
     * Gets the name of the ingredient.
     * @return the ingredient name
     */
    String getName();

    /**
     * Gets the array of effects associated with the ingredient.
     * Some effects may be null if not yet discovered.
     * @return an array of up to four effects
     */
    IEffect[] getEffects();

    /**
     * Learns a new effect for the ingredient.
     * @param effect the effect to learn
     */
    void learnEffect(IEffect effect);

    /**
     * Checks if the ingredient has a specific effect.
     * @param effect the effect to check
     * @return true if the ingredient has the effect, false otherwise
     */
    boolean hasEffect(IEffect effect);
}
