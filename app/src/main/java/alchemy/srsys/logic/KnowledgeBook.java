package alchemy.srsys.logic;

import alchemy.srsys.object.*;
import alchemy.srsys.object.IStubDatabase;

import java.util.Map;

public class KnowledgeBook implements IKnowledgeBook {
    private IStubDatabase database;

    public KnowledgeBook(IStubDatabase database) {
        this.database = database;
    }

    @Override
    public void updateIngredientKnowledge(IIngredient ingredient) {
        if (ingredient != null) {
            database.updateKnowledgeBook(ingredient);
        }
    }

    @Override
    public Map<String, IEffect[]> getKnownIngredients() {
        return database.getKnowledgeBook();
    }

    @Override
    public boolean isEffectKnown(IIngredient ingredient, IEffect effect) {
        Map<String, IEffect[]> knownIngredients = getKnownIngredients();
        IEffect[] knownEffects = knownIngredients.get(ingredient.getName());

        if (knownEffects == null) return false;

        for (IEffect knownEffect : knownEffects) {
            if (knownEffect != null && knownEffect.getId() == effect.getId()) {
                return true;
            }
        }
        return false;
    }
}
