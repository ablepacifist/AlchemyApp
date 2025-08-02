package alchemy.srsys.object.DTO;

import java.io.Serializable;

public class BrewResponse implements Serializable {
    private String message;
    private PotionDto potion;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public PotionDto getPotion() { return potion; }
    public void setPotion(PotionDto potion) { this.potion = potion; }
}
