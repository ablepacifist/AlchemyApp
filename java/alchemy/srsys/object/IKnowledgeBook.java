package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IEffect;
import java.util.Map;

public interface IKnowledgeBook {
    void updateIngredientKnowledge(IIngredient ingredient);
    Map<String, IEffect[]> getKnownIngredients();
    boolean isEffectKnown(IIngredient ingredient, IEffect effect);
}
