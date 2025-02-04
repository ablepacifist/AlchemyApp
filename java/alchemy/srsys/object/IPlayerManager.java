package alchemy.srsys.object;

import alchemy.srsys.object.*;
import java.util.List;
import java.util.Map;

public interface IPlayerManager {
    // Inventory methods
    void addIngredientToInventory(IIngredient ingredient);
    void removeIngredientFromInventory(IIngredient ingredient);
    List<IIngredient> getInventory();

    // KnowledgeBook methods
    void updateKnowledgeBook(IIngredient ingredient);
    Map<String, IEffect[]> getKnownIngredients();
    boolean isEffectKnown(IIngredient ingredient, IEffect effect);

    // Mixing methods
    IPotion mixIngredients(IIngredient ingredient1, IIngredient ingredient2);

    // Consumption methods
    void consumeIngredient(IIngredient ingredient);
}
