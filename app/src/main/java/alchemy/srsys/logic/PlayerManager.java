package alchemy.srsys.logic;

import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.object.*;
import alchemy.srsys.object.IStubDatabase;

import java.util.List;
import java.util.Map;

public class PlayerManager implements IPlayerManager {
    private Player player;
    private IStubDatabase database;
    private IInventory inventory;
    private IKnowledgeBook knowledgeBook;
    private IMixing mixing;

    public PlayerManager(Player player) {
        this.player = player;
        this.database = new StubDatabase();
        this.inventory = new Inventory(database, player);
        this.knowledgeBook = new KnowledgeBook(database, player);
        this.mixing = new Mixing();
       //TODO: this.consumption = new Consumption();
    }

    // Inventory methods
    @Override
    public void addIngredientToInventory(IIngredient ingredient) {
        inventory.addIngredient(ingredient);
    }

    @Override
    public void removeIngredientFromInventory(IIngredient ingredient) {
        inventory.removeIngredient(ingredient);
    }

    @Override
    public List<IIngredient> getInventory() {
        return inventory.getIngredients();
    }

    // KnowledgeBook methods
    @Override
    public void updateKnowledgeBook(IIngredient ingredient) {
        knowledgeBook.updateIngredientKnowledge(ingredient);
    }

    @Override
    public Map<String, IEffect[]> getKnownIngredients() {
        return knowledgeBook.getKnownIngredients();
    }

    @Override
    public boolean isEffectKnown(IIngredient ingredient, IEffect effect) {
        return knowledgeBook.isEffectKnown(ingredient, effect);
    }

    // Mixing methods
    @Override
    public IPotion mixIngredients(IIngredient ingredient1, IIngredient ingredient2) {
        IPotion potion = mixing.mixIngredients(ingredient1, ingredient2);
        if (potion != null) {
            updateKnowledgeBook(ingredient1);
            updateKnowledgeBook(ingredient2);
            removeIngredientFromInventory(ingredient1);
            removeIngredientFromInventory(ingredient2);
            // Optionally add the potion to inventory or handle further
        }
        return potion;
    }

    // Consumption methods
    @Override
    public void consumeIngredient(IIngredient ingredient) {
       //TODO: consumption.eatIngredient(ingredient, knowledgeBook);
        removeIngredientFromInventory(ingredient);
    }
}
