package alchemy.srsys.logic;

import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.*;

import java.util.Map;

public class KnowledgeBook implements IKnowledgeBook {
    private IStubDatabase database;
    private Player player;

    public KnowledgeBook(IStubDatabase database, Player player) {
        this.database = database;
        this.player = player;
    }

    @Override
    public void updateIngredientKnowledge(IIngredient ingredient) {
        database.updateKnowledgeBook(player.getName(), ingredient);
    }

    @Override
    public Map<String, IEffect[]> getKnownIngredients() {
        return database.getKnowledgeBook(player.getName());
    }

    @Override
    public boolean isEffectKnown(IIngredient ingredient, IEffect effect) {
        IEffect[] knownEffects = getKnownIngredients().get(ingredient.getName());
        if (knownEffects == null) {
            return false;
        }
        for (IEffect knownEffect : knownEffects) {
            if (knownEffect != null && knownEffect.getId() == effect.getId()) {
                return true;
            }
        }
        return false;
    }
}
