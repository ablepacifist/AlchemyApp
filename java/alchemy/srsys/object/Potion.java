package alchemy.srsys.object;

import java.util.ArrayList;
import java.util.List;

public class Potion implements IPotion {
    private String name;
    private List<IEffect> effects;
    private IIngredient ingredient1;
    private IIngredient ingredient2;

    public Potion(IIngredient ingredient1, IIngredient ingredient2) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.effects = findCommonEffects();
        this.name = generatePotionName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IEffect[] getEffects() {
        return effects.toArray(new IEffect[0]);
    }

    @Override
    public IIngredient getIngredient1() {
        return ingredient1;
    }

    @Override
    public IIngredient getIngredient2() {
        return ingredient2;
    }

    private List<IEffect> findCommonEffects() {
        List<IEffect> common = new ArrayList<>();
        for (IEffect effect1 : ingredient1.getEffects()) {
            if (effect1 == null) continue;
            for (IEffect effect2 : ingredient2.getEffects()) {
                if (effect2 == null) continue;
                if (effect1.getId() == effect2.getId() && !common.contains(effect1)) {
                    common.add(effect1);
                }
            }
        }
        return common;
    }

    private String generatePotionName() {
        if (effects.isEmpty()) {
            return "Unknown Potion";
        } else {
            StringBuilder sb = new StringBuilder();
            for (IEffect effect : effects) {
                sb.append(effect.getTitle()).append(" ");
            }
            sb.append("Potion");
            return sb.toString().trim();
        }
    }
}
