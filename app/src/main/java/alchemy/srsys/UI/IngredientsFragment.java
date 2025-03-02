package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alchemy.srsys.R;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.Player;

public class IngredientsFragment extends Fragment {

    private Player player;
    private GameManager gameManager;
    private RecyclerView recyclerView;
    private IngredientsAdapter ingredientsAdapter;

    public IngredientsFragment(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        recyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get ingredients data from player's inventory
        List<IngredientEntry> ingredientEntries = getIngredientEntries();

        // Set up adapter
        ingredientsAdapter = new IngredientsAdapter(ingredientEntries);
        recyclerView.setAdapter(ingredientsAdapter);

        return view;
    }

    private List<IngredientEntry> getIngredientEntries() {
        List<IngredientEntry> entries = new ArrayList<>();

        // Retrieve all ingredients from the player's inventory
        IInventory inventory = player.getInventory();
        Map<IIngredient, Integer> allIngredients = inventory.getIngredients();

        // For each ingredient, get its known effects and add to entries
        for (Map.Entry<IIngredient, Integer> entry : allIngredients.entrySet()) {
            IIngredient ingredient = entry.getKey();
            int quantity = entry.getValue();
            List<IEffect> knownEffects = player.getKnowledgeBook().getKnownEffects(ingredient);
            entries.add(new IngredientEntry(ingredient.getName(), quantity, knownEffects));
        }

        return entries;
    }

    // Inner class to represent an ingredient entry
    static class IngredientEntry {
        String ingredientName;
        int quantity;
        List<IEffect> knownEffects;

        IngredientEntry(String ingredientName, int quantity, List<IEffect> knownEffects) {
            this.ingredientName = ingredientName;
            this.quantity = quantity;
            this.knownEffects = knownEffects;
        }
    }
}
