package alchemy.srsys.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.Inventory;
import alchemy.srsys.object.Potion;

public class PotionManager {
    private final IStubDatabase db;

    public PotionManager(IStubDatabase db) {
        this.db = db;
    }

    /**
     * Brew a potion by combining two ingredients from a player's inventory.
     * The potion is created if the two ingredients share at least one effect.
     */
    public Potion brewPotion(int playerId, IIngredient ingredient1, IIngredient ingredient2) {
        // Check that the two ingredients are not the same.
        if (ingredient1.getId() == ingredient2.getId()) {
            System.out.println("Cannot brew a potion with the same ingredient twice.");
            return null;
        }

        // Get the player's inventory from the domain logic.
        IInventory inventory = db.getPlayerInventory(playerId);
        Map<IIngredient, Integer> ingredientsMap = inventory.getIngredients();

        // Determine the available quantity for each ingredient by comparing IDs.
        int quantity1 = 0;
        int quantity2 = 0;
        for (Map.Entry<IIngredient, Integer> entry : ingredientsMap.entrySet()) {
            IIngredient ing = entry.getKey();
            if (ing.getId() == ingredient1.getId()) {
                quantity1 = entry.getValue();
            }
            if (ing.getId() == ingredient2.getId()) {
                quantity2 = entry.getValue();
            }
        }

        // Check that both ingredients exist with at least one unit available.
        if (quantity1 < 1 || quantity2 < 1) {
            System.out.println("Missing one or more ingredients. No potion brewed.");
            return null;
        }

        // Retrieve the effects for each ingredient.
        List<IEffect> effects1 = db.getEffectsForIngredient(ingredient1.getId());
        List<IEffect> effects2 = db.getEffectsForIngredient(ingredient2.getId());

        // Determine the shared effects.
        List<IEffect> sharedEffects = new ArrayList<>();
        for (IEffect e1 : effects1) {
            for (IEffect e2 : effects2) {
                if (e1.getId() == e2.getId()) {
                    sharedEffects.add(e1);
                    break;
                }
            }
        }

        // Remove one unit of each ingredient.
        db.removeIngredientFromPlayerInventory(playerId, ingredient1, 1);
        db.removeIngredientFromPlayerInventory(playerId, ingredient2, 1);

        if (sharedEffects.isEmpty()) {
            System.out.println("No shared effects. No potion brewed.");
            return null;
        } else {
            // Create the potion.
            int potionId = db.getNextPotionId();
            String potionName = "Potion of " + generatePotionName(sharedEffects);
            Potion potion = new Potion(potionId, potionName, sharedEffects, ingredient1, ingredient2);

            // Insert the potion into the POTIONS table.
            db.addPotion(potion);
            // Add the potion to the player's inventory.
            db.addPotionToPlayerInventory(playerId, potion, 1);
            // Update the player's knowledge book for both ingredients.
            for (IEffect effect : sharedEffects) {
                db.addKnowledgeEntry(playerId, ingredient1, effect);
                db.addKnowledgeEntry(playerId, ingredient2, effect);
            }
            System.out.println("Brewed potion: " + potionName + " for player " + playerId);
            return potion;
        }
    }


    /**
     * Helper method to generate a potion name from shared effects.
     */
    private String generatePotionName(List<IEffect> sharedEffects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sharedEffects.size(); i++) {
            sb.append(sharedEffects.get(i).getTitle());
            if (i < sharedEffects.size() - 1) {
                sb.append(" & ");
            }
        }
        return sb.toString();
    }
    /**
     * Forage: Randomly selects an ingredient from the master list and adds one unit
     * of it to the player's inventory.
     *
     * @param playerId the ID of the player for whom to forage
     * @return the name of the foraged ingredient, or an empty string if none available.
     */
    public String forage(int playerId) {
        List<IIngredient> masterIngredients = db.getAllIngredients();
        if (masterIngredients.isEmpty()) {
            System.out.println("No ingredients available to forage.");
            return "";
        }
        Random rand = new Random();
        IIngredient randomIngredient = masterIngredients.get(rand.nextInt(masterIngredients.size()));
        db.addIngredientToPlayerInventory(playerId, randomIngredient, 1);
        System.out.println("Foraged " + randomIngredient.getName() + " for player " + playerId);
        return randomIngredient.getName();
    }
}
