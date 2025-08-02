package alchemy.srsys.object.DTO;

import java.io.Serializable;
import java.util.List;

public class KnowledgeEntry implements Serializable {
    private int ingredientId;
    private String ingredientName;
    private List<EffectDto> effects;

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public List<EffectDto> getEffects() { return effects; }
    public void setEffects(List<EffectDto> effects) { this.effects = effects; }
}
