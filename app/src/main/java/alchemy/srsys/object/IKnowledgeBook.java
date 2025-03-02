package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IEffect;

import java.util.List;
import java.util.Map;

public interface IKnowledgeBook {

    boolean isEffectKnown(IIngredient ingredient, IEffect effect);

    /**
     * Learns effects for a specific ingredient.
     * @param ingredient the ingredient
     * @param matchedEffects the effects to learn
     */
    void learnEffects(IIngredient ingredient, List<IEffect> matchedEffects);

    /**
     * Gets known effects for an ingredient.
     * @param ingredient the ingredient
     * @return list of known effects (null where unknown)
     */
    List<IEffect> getKnownEffects(IIngredient ingredient);

    void learnEffect(IIngredient ingredient, IEffect effect);
}

