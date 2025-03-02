package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import alchemy.srsys.R;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.Mixing;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.Player;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class BrewFragment extends Fragment {

    private Player player;
    private GameManager gameManager;

    private Spinner ingredient1Spinner;
    private Spinner ingredient2Spinner;
    private Button brewButton;

    public BrewFragment(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew, container, false);

        ingredient1Spinner = view.findViewById(R.id.ingredient1Spinner);
        ingredient2Spinner = view.findViewById(R.id.ingredient2Spinner);
        brewButton = view.findViewById(R.id.brewPotionButton);

        // Populate spinners with ingredients
        populateIngredientSpinners();

        // Set up brew button listener
        brewButton.setOnClickListener(v -> brewPotion());

        return view;
    }

    private void populateIngredientSpinners() {
        List<String> ingredientNames = new ArrayList<>();
        for (IIngredient ingredient : player.getInventory().getIngredients().keySet()) {
            ingredientNames.add(ingredient.getName());
        }

        if (ingredientNames.isEmpty()) {
            ingredientNames.add("No Ingredients");
            brewButton.setEnabled(false);
        }

        // Create ArrayAdapter using the ingredient names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, ingredientNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ingredient1Spinner.setAdapter(adapter);
        ingredient2Spinner.setAdapter(adapter);
    }


    private void brewPotion() {
        String ingredientName1 = ingredient1Spinner.getSelectedItem().toString();
        String ingredientName2 = ingredient2Spinner.getSelectedItem().toString();

        if (ingredientName1.equals("No Ingredients") || ingredientName2.equals("No Ingredients")) {
            Toast.makeText(getContext(), "You have no ingredients to brew.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ingredientName1.equals(ingredientName2)) {
            Toast.makeText(getContext(), "Select two different ingredients.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mix ingredients via GameManager
        IPotion potion = gameManager.mixIngredients(player, ingredientName1, ingredientName2);

        if (potion != null) {
            Toast.makeText(getContext(), "Potion " + potion.getName() + " created!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Mixing failed. No matching effects or insufficient ingredients.", Toast.LENGTH_LONG).show();
        }

        // Refresh ingredient spinners
        populateIngredientSpinners();

        // Notify the MainActivity to refresh InventoryFragment
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshInventoryFragment();
        }
    }
}
