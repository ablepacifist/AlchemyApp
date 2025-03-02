package alchemy.srsys.object;

import java.util.List;

public class Ingredient implements IIngredient {
    private int id;
    private String name;
    private List<IEffect> effects;

    public Ingredient(int id, String name, List<IEffect> effects) {
        this.id = id;
        this.name = name;
        this.effects = effects;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<IEffect> getEffects() {
        return effects;
    }

    @Override
    public void learnEffect(IEffect effect) {

    }

    @Override
    public boolean hasEffect(IEffect effect) {
        return false;
    }
}
