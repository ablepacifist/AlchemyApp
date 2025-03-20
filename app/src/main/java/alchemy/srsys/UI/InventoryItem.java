package alchemy.srsys.UI;

import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IPotion;


import java.io.Serializable;

import java.util.List;

public class InventoryItem implements Serializable {
    private int id;
    private String name;
    private String type; // "Ingredient" or "Potion"
    private int quantity;
    private IIngredient ingredient; // non-null if type equals "Ingredient"
    private IPotion potion;         // non-null if type equals "Potion"

    // Constructor for an ingredient item.
    public InventoryItem(IIngredient ingredient, int quantity) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.type = "Ingredient";
        this.quantity = quantity;
        this.ingredient = ingredient;
    }

    // Constructor for a potion item.
    public InventoryItem(IPotion potion, int quantity) {
        this.id = potion.getId();
        this.name = potion.getName();
        this.type = "Potion";
        this.quantity = quantity;
        this.potion = potion;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public IIngredient getIngredient() { return ingredient; }
    public IPotion getPotion() { return potion; }

    /**
     * Returns a description of the ingredient’s known effects.
     */
    public String getKnownEffectsDescription() {
        if ("Ingredient".equalsIgnoreCase(type) && ingredient != null) {
            List<IEffect> effects = ingredient.getEffects();
            if (effects != null && !effects.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (IEffect effect : effects) {
                    sb.append(effect.getTitle()).append(", ");
                }
                // Remove trailing comma and space.
                sb.setLength(sb.length() - 2);
                return sb.toString();
            }
        }
        return "";
    }
}
