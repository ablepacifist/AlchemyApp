package alchemy.srsys.logic;

import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.Player;
import java.util.List;

public class Inventory implements IInventory {
    private IStubDatabase database;
    private Player player;

    public Inventory(IStubDatabase database, Player player) {
        this.database = database;
        this.player = player;
    }

    @Override
    public void addIngredient(IIngredient ingredient) {
        database.addIngredientToInventory(player.getName(), ingredient);
    }

    @Override
    public void removeIngredient(IIngredient ingredient) {
        database.removeIngredientFromInventory(player.getName(), ingredient);
    }

    @Override
    public List<IIngredient> getIngredients() {
        return database.getInventoryIngredients(player.getName());
    }

    @Override
    public boolean contains(IIngredient ingredient) {
        return getIngredients().contains(ingredient);
    }
}
