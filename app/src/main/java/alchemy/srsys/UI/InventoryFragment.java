package alchemy.srsys.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; 

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView recyclerView;
    private InventoryAdapter adapter;

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

        // Initialize RecyclerView and set its layout manager.
        recyclerView = view.findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the GameManagerService.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        PlayerManager playerManager = gameManager.getPlayerManager();

        // Fetch the player's inventory from the logic layer.
        IInventory domainInventory = playerManager.getInventory(playerId);

        // Map the domain inventory (ingredients and potions) to a list of InventoryItem models for the UI.
        List<InventoryItem> inventoryItems = UIDataMapper.toInventoryItemList(domainInventory);

        // Set up the adapter with a click listener that navigates to the detail activity.
        adapter = new InventoryAdapter(inventoryItems, item -> {
            // Launch the detail activity for the selected item.
            Intent intent = new Intent(getContext(), InventoryItemDetailActivity.class);
            intent.putExtra("PLAYER_ID", playerId); // Pass the player ID.
            intent.putExtra("INVENTORY_ITEM", item); // Pass the selected inventory item.
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

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
        IInventory domainInventory = gameManager.getPlayerManager().getInventory(playerId);
        List<InventoryItem> updatedItems = UIDataMapper.toInventoryItemList(domainInventory);
        adapter.setInventoryItems(updatedItems);
        adapter.notifyDataSetChanged();
    }
}
