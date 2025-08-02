package alchemy.srsys.object.DTO;

import java.io.Serializable;
import java.util.List;

public class IngredientDto implements Serializable {
    private int id;
    private String name;
    private List<EffectDto> effects;
    private int quantity;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<EffectDto> getEffects() { return effects; }
    public void setEffects(List<EffectDto> effects) { this.effects = effects; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
