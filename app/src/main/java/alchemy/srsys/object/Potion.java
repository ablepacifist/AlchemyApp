package alchemy.srsys.object;

import java.io.Serializable;
import java.util.List;

public class Potion implements IPotion , Serializable {
    private int id;
    private String name;
    private List<IEffect> effects;
    private IIngredient ingredient1;
    private IIngredient ingredient2;

    public Potion(int id, String name, List<IEffect> effects, IIngredient ingredient1, IIngredient ingredient2) {
        this.id = id;
        this.name = name;
        this.effects = effects;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
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
    public IIngredient getIngredient1() {
        return ingredient1;
    }

    @Override
    public IIngredient getIngredient2() {
        return ingredient2;
    }
}
