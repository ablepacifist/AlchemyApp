package alchemy.srsys.ui;

import java.util.List;

public class KnownIngredientItem {

    private String ingredientName;
    private List<String> effects;

    public KnownIngredientItem(String ingredientName, List<String> effects) {
        this.ingredientName = ingredientName;
        this.effects = effects;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public List<String> getEffects() {
        return effects;
    }

    public String getEffectsString() {
        if (effects.isEmpty()) {
            return "No known effects";
        }
        return String.join(", ", effects);
    }
}
