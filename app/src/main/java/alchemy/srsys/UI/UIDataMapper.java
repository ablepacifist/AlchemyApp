package alchemy.srsys.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.IPotion;



public class UIDataMapper {

    // Converts an IInventory to a List of InventoryItem for UI display.
    public static List<InventoryItem> toInventoryItemList(IInventory inventory) {
        List<InventoryItem> itemList = new ArrayList<>();
        // Convert ingredients.
        for (Map.Entry<IIngredient, Integer> entry : inventory.getIngredients().entrySet()) {
            itemList.add(new InventoryItem(entry.getKey(), entry.getValue()));
        }
        // Convert potions.
        for (Map.Entry<IPotion, Integer> entry : inventory.getPotions().entrySet()) {
            itemList.add(new InventoryItem(entry.getKey(), entry.getValue()));
        }
        return itemList;
    }

    // Converts an IKnowledgeBook to a list of KnowledgeEntry UI models.
    public static List<KnowledgeEntry> toKnowledgeEntryList(IKnowledgeBook kb, IStubDatabase db) {
        List<KnowledgeEntry> entries = new ArrayList<>();

        // Get the map of ingredient IDs to learned effects from the knowledge book.
        Map<Integer, List<IEffect>> knowledgeMap = kb.getKnowledge();
        if (knowledgeMap != null) {
            for (Map.Entry<Integer, List<IEffect>> entry : knowledgeMap.entrySet()) {
                int ingredientId = entry.getKey();

                // Fetch the ingredient name using the database.
                IIngredient ingredient = db.getIngredientById(ingredientId); // New method in IStubDatabase
                String ingredientName = ingredient != null ? ingredient.getName() : "Unknown Ingredient";

                // Build the list of effect descriptions for this ingredient.
                StringBuilder sb = new StringBuilder();
                for (IEffect effect : entry.getValue()) {
                    sb.append(effect.getTitle()).append(", ");
                }
                String knownEffects = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : ""; // Remove trailing comma.

                // Create a knowledge entry for the UI model.
                entries.add(new KnowledgeEntry(ingredientId, ingredientName, knownEffects));
            }
        }
        return entries;
    }

}

