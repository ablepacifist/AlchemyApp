package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alchemy.srsys.R;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.Potion;


import alchemy.srsys.application.MyApp;




public class BrewFragment extends Fragment {
    private static final String ARG_PLAYER_ID = "playerId";
    private int playerId;
    private Spinner ingredient1Spinner, ingredient2Spinner;
    private Button brewPotionButton;
    // We no longer hold a PlayerManager reference here.
    // Instead, we go through GameManagerService when brewing a potion.
    private GameManagerService gameManager;

    public static BrewFragment newInstance(int playerId) {
        BrewFragment fragment = new BrewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_ID, playerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            playerId = getArguments().getInt(ARG_PLAYER_ID);
        }
        // Obtain the GameManagerService instance.
        gameManager = MyApp.getGameManagerService(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brew, container, false);
        ingredient1Spinner = view.findViewById(R.id.ingredient1Spinner);
        ingredient2Spinner = view.findViewById(R.id.ingredient2Spinner);
        brewPotionButton = view.findViewById(R.id.brewPotionButton);

        populateIngredientSpinners();

        brewPotionButton.setOnClickListener(v -> {
            IIngredient ingr1 = (IIngredient) ingredient1Spinner.getSelectedItem();
            IIngredient ingr2 = (IIngredient) ingredient2Spinner.getSelectedItem();
            // Now brew the potion by delegating through GameManagerService.
            Potion potion = gameManager.getPotionManager().brewPotion(playerId, ingr1, ingr2);
            if (potion != null) {
                Toast.makeText(getContext(), "Potion Brewed: " + potion.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Brew failed: No shared effects", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshIngredients();
    }

    private void populateIngredientSpinners() {
        // Get the player's inventory from the logic layer.
        IInventory domainInventory = gameManager.getPlayerManager().getInventory(playerId);

        // Extract only the ingredients into a list.
        List<IIngredient> ingredientList = new ArrayList<>();
        for (Map.Entry<IIngredient, Integer> entry : domainInventory.getIngredients().entrySet()) {
            ingredientList.add(entry.getKey());
        }

        // Create and assign adapters for both spinners.
        ArrayAdapter<IIngredient> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ingredientList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredient1Spinner.setAdapter(adapter);
        ingredient2Spinner.setAdapter(adapter);
    }


    private void refreshIngredients() {
        // Simply repopulate the spinners using the latest inventory.
        populateIngredientSpinners();
    }
}

