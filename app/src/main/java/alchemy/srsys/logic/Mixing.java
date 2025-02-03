package alchemy.srsys.logic;

import alchemy.srsys.object.*;

public class Mixing implements IMixing {

    @Override
    public IPotion mixIngredients(IIngredient ingredient1, IIngredient ingredient2) {
        IPotion potion = new Potion(ingredient1, ingredient2);

        if (potion.getEffects().length > 0) {
            // Mixing is successful
            return potion;
        } else {
            // Mixing failed
            return null;
        }
    }
}
