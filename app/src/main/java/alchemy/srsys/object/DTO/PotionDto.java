package alchemy.srsys.object.DTO;

import java.io.Serializable;
import java.util.List;

public class PotionDto implements Serializable {
    private int id;
    private String name;
    private String description;
    private int duration;
    private int brewLevel;
    private String dice;
    private List<EffectDto> effects;
    private int quantity;    // present on inventory responses

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getBrewLevel() { return brewLevel; }
    public void setBrewLevel(int brewLevel) { this.brewLevel = brewLevel; }

    public String getDice() { return dice; }
    public void setDice(String dice) { this.dice = dice; }

    public List<EffectDto> getEffects() { return effects; }
    public void setEffects(List<EffectDto> effects) { this.effects = effects; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
