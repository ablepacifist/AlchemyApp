package alchemy.srsys.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.*;

public class Mixing implements IMixing {
    private IStubDatabase database;

    public Mixing(IStubDatabase database) {
        this.database = database;
    }

    @Override
    public IPotion mixIngredients(Player player, IIngredient ingredient1, IIngredient ingredient2) {
        IInventory inventory = player.getInventory();

        // Check if the player has at least one of each ingredient
        if (!inventory.containsIngredient(ingredient1) || !inventory.containsIngredient(ingredient2)) {
            // Player does not have enough ingredients
            return null;
        }

        // Check the quantities of the ingredients
        Map<IIngredient, Integer> ingredients = inventory.getIngredients();
        int quantity1 = ingredients.getOrDefault(ingredient1, 0);
        int quantity2 = ingredients.getOrDefault(ingredient2, 0);

        if (quantity1 < 1 || quantity2 < 1) {
            // Not enough quantity of one or both ingredients
            return null;
        }

        // **Remove ingredients from inventory regardless of mixing outcome**
        inventory.removeIngredient(ingredient1, 1); // Remove one unit of ingredient1
        inventory.removeIngredient(ingredient2, 1); // Remove one unit of ingredient2

        // Find matching effects between ingredients
        List<IEffect> matchingEffects = new ArrayList<>();
        for (IEffect effect1 : ingredient1.getEffects()) {
            if (effect1 == null) continue;
            for (IEffect effect2 : ingredient2.getEffects()) {
                if (effect2 == null) continue;
                if (effect1.getId() == effect2.getId()) {
                    if (!matchingEffects.contains(effect1)) {
                        matchingEffects.add(effect1);
                    }
                }
            }
        }

        if (!matchingEffects.isEmpty()) {
            // Create potion
            int potionId = database.getNextPotionId();
            String potionName = generatePotionName(matchingEffects);
            Potion potion = new Potion(potionId, potionName, matchingEffects, ingredient1, ingredient2);

            // Update player's inventory
            inventory.addPotion(potion, 1); // Add one unit of the potion

            // Update knowledge book
            IKnowledgeBook knowledgeBook = player.getKnowledgeBook();
            knowledgeBook.learnEffects(ingredient1, matchingEffects);
            knowledgeBook.learnEffects(ingredient2, matchingEffects);

            return potion;
        } else {
            // No matching effects, mixing fails
            return null;
        }
    }

    private String generatePotionName(List<IEffect> effects) {
        StringBuilder sb = new StringBuilder();
        for (IEffect effect : effects) {
            sb.append(effect.getTitle()).append(" ");
        }
        sb.append("Potion");
        return sb.toString().trim();
    }
}
