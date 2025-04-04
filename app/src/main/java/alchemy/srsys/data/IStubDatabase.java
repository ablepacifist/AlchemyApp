package alchemy.srsys.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.Potion;

public interface IStubDatabase {

    int getNextPotionId();

    int getNextPlayerId();

    IIngredient getIngredientByName(String name);

    List<IIngredient> getAllIngredients();

    void addPlayer(Player player);

    Player getPlayer(int playerId);

    Player getPlayerByUsername(String username);

    Collection<Player> getAllPlayers();

    void addPotionToPlayerInventory(int playerId, Potion potion, int quantity);

    void addIngredientToPlayerInventory(int playerId, IIngredient ingredient, int quantity);

    void removeIngredientFromPlayerInventory(int playerId, IIngredient ingredient, int quantity);

    IInventory getPlayerInventory(int playerId);

    void addKnowledgeEntry(int playerId, IIngredient ingredient, IEffect effect);

    void updateKnowledgeBook(int playerId, IIngredient ingredient);

    public IKnowledgeBook getKnowledgeBook(int playerId);
    List<IEffect> getEffectsForIngredient(int ingredientId);

    void addIngredient(IIngredient ingWithShared1);

    void addEffect(IEffect uniqueEffect2);
    void removePotionFromPlayerInventory(int playerId, Potion potion, int quantity);
    IIngredient getIngredientById(int ingredientId);
    void addPotion(Potion potion);

    void updatePlayerLevel(int id, int level);
}
