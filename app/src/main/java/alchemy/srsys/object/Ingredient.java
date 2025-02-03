package alchemy.srsys.object;

import java.util.Arrays;

public class Ingredient implements IIngredient {
    private String name;
    private IEffect[] effects;

    public Ingredient(String name) {
        this.name = name;
        this.effects = new IEffect[4]; // Up to 4 possible effects
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IEffect[] getEffects() {
        return effects;
    }

    @Override
    public void learnEffect(IEffect effect) {
        for (int i = 0; i < effects.length; i++) {
            if (effects[i] == null) {
                effects[i] = effect;
                break;
            } else if (effects[i].getId() == effect.getId()) {
                // Effect already known
                break;
            }
        }
    }

    @Override
    public boolean hasEffect(IEffect effect) {
        return Arrays.stream(effects)
                .anyMatch(e -> e != null && e.getId() == effect.getId());
    }
}
