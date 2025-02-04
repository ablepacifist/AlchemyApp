package alchemy.srsys.object;

import alchemy.srsys.object.IIngredient;
import java.util.List;

public interface IInventory {
    void addIngredient(IIngredient ingredient);
    void removeIngredient(IIngredient ingredient);
    List<IIngredient> getIngredients();
    boolean contains(IIngredient ingredient);
}
