package alchemy.srsys.logic;

import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.IEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBook implements IKnowledgeBook {
    private Map<Integer, List<IEffect>> ingredientKnowledge;

    public KnowledgeBook(List<IIngredient> allIngredients) {
        ingredientKnowledge = new HashMap<>();
        for (IIngredient ingredient : allIngredients) {
            ingredientKnowledge.put(ingredient.getId(), new ArrayList<>(Arrays.asList(new IEffect[4])));
        }
    }

    @Override
    public List<IEffect> getKnownEffects(IIngredient ingredient) {
        return ingredientKnowledge.getOrDefault(ingredient.getId(), new ArrayList<>(Arrays.asList(new IEffect[4])));
    }

    @Override
    public void learnEffect(IIngredient ingredient, IEffect effect) {

    }

    @Override
    public boolean isEffectKnown(IIngredient ingredient, IEffect effect) {
        return false;
    }

    @Override
    public void learnEffects(IIngredient ingredient, List<IEffect> effects) {
        List<IEffect> knownEffects = ingredientKnowledge.get(ingredient.getId());
        if (knownEffects == null) {
            knownEffects = new ArrayList<>(Arrays.asList(new IEffect[4]));
            ingredientKnowledge.put(ingredient.getId(), knownEffects);
        }

        for (IEffect effect : effects) {
            for (int i = 0; i < knownEffects.size(); i++) {
                if (knownEffects.get(i) == null) {
                    knownEffects.set(i, effect);
                    break;
                }
            }
        }
    }
}
