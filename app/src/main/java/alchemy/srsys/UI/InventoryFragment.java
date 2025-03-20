package alchemy.srsys.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; 

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.IInventory;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class InventoryFragment extends Fragment {
    private static final String ARG_PLAYER_ID = "playerId";
    private int playerId;
    private RecyclerView ingredientsRecyclerView;
    private RecyclerView potionsRecyclerView;
    private InventoryAdapter ingredientsAdapter;
    private InventoryAdapter potionsAdapter;

    public static InventoryFragment newInstance(int playerId) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_ID, playerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerId = getArguments().getInt(ARG_PLAYER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout.
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Initialize RecyclerViews and set their layout managers.
        ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);
        potionsRecyclerView = view.findViewById(R.id.potionsRecyclerView);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        potionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the GameManagerService.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        PlayerManager playerManager = gameManager.getPlayerManager();

        // Fetch the player's inventory from the logic layer.
        IInventory domainInventory = playerManager.getInventory(playerId);

        // Split the inventory into ingredients and potions.
        List<InventoryItem> inventoryItems = UIDataMapper.toInventoryItemList(domainInventory);
        List<InventoryItem> ingredientItems = new ArrayList<>();
        List<InventoryItem> potionItems = new ArrayList<>();

        for (InventoryItem item : inventoryItems) {
            if ("Ingredient".equalsIgnoreCase(item.getType())) {
                ingredientItems.add(item);
            } else if ("Potion".equalsIgnoreCase(item.getType())) {
                potionItems.add(item);
            }
        }

        // Set up the adapters with click listeners.
        ingredientsAdapter = new InventoryAdapter(ingredientItems, item -> {
            // Navigate to the detail activity for the selected ingredient.
            Intent intent = new Intent(getContext(), InventoryItemDetailActivity.class);
            intent.putExtra("PLAYER_ID", playerId);
            intent.putExtra("INVENTORY_ITEM", item);
            startActivity(intent);
        });

        potionsAdapter = new InventoryAdapter(potionItems, item -> {
            // Navigate to the detail activity for the selected potion.
            Intent intent = new Intent(getContext(), InventoryItemDetailActivity.class);
            intent.putExtra("PLAYER_ID", playerId);
            intent.putExtra("INVENTORY_ITEM", item);
            startActivity(intent);
        });

        // Set the adapters to the RecyclerViews.
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        potionsRecyclerView.setAdapter(potionsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInventory();
    }

    private void refreshInventory() {
        // Refresh the inventory by retrieving it from the logic layer and mapping it for UI.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        PlayerManager playerManager = gameManager.getPlayerManager();
        IInventory domainInventory = playerManager.getInventory(playerId);

        // Split the inventory into ingredients and potions.
        List<InventoryItem> inventoryItems = UIDataMapper.toInventoryItemList(domainInventory);
        List<InventoryItem> ingredientItems = new ArrayList<>();
        List<InventoryItem> potionItems = new ArrayList<>();

        for (InventoryItem item : inventoryItems) {
            if ("Ingredient".equalsIgnoreCase(item.getType())) {
                ingredientItems.add(item);
            } else if ("Potion".equalsIgnoreCase(item.getType())) {
                potionItems.add(item);
            }
        }

        // Update the adapters with the refreshed data.
        ingredientsAdapter.setInventoryItems(ingredientItems);
        potionsAdapter.setInventoryItems(potionItems);

        // Notify the adapters of the changes.
        ingredientsAdapter.notifyDataSetChanged();
        potionsAdapter.notifyDataSetChanged();
    }
}
