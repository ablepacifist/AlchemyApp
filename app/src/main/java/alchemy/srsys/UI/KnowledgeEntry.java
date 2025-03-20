package alchemy.srsys.UI;

import java.io.Serializable;

public class KnowledgeEntry implements Serializable {
    private int ingredientId;
    private String ingredientName;
    private String knownEffects;

    public KnowledgeEntry(int ingredientId, String ingredientName, String knownEffects) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.knownEffects = knownEffects;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getKnownEffects() {
        return knownEffects;
    }
}
