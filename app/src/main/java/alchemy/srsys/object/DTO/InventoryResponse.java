package alchemy.srsys.object.DTO;

import java.io.Serializable;
import java.util.List;

public class InventoryResponse implements Serializable {
    private List<IngredientDto> ingredients;
    private List<PotionDto> potions;

    public List<IngredientDto> getIngredients() { return ingredients; }
    public void setIngredients(List<IngredientDto> ingredients) { this.ingredients = ingredients; }

    public List<PotionDto> getPotions() { return potions; }
    public void setPotions(List<PotionDto> potions) { this.potions = potions; }
}
