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
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IInventory;
import alchemy.srsys.object.IPotion;
import alchemy.srsys.object.Player;

public class InventoryFragment extends Fragment {

    private Player player;
    private GameManager gameManager;
    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;

    public InventoryFragment(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        recyclerView = view.findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get inventory items
        List<InventoryItem> inventoryItems = getInventoryItems();

        // Set up adapter
        inventoryAdapter = new InventoryAdapter(inventoryItems);
        recyclerView.setAdapter(inventoryAdapter);

        return view;
    }

    private List<InventoryItem> getInventoryItems() {
        List<InventoryItem> items = new ArrayList<>();

        IInventory inventory = player.getInventory();

        // Get ingredients with quantities
        for (Map.Entry<IIngredient, Integer> entry : inventory.getIngredients().entrySet()) {
            items.add(new InventoryItem(entry.getKey().getName(), "Ingredient", entry.getValue()));
        }

        // Get potions with quantities
        for (Map.Entry<IPotion, Integer> entry : inventory.getPotions().entrySet()) {
            items.add(new InventoryItem(entry.getKey().getName(), "Potion", entry.getValue()));
        }

        return items;
    }

    public void refreshInventory() {
        // Re-fetch inventory items
        List<InventoryItem> inventoryItems = getInventoryItems();

        // Update the adapter's data
        if (inventoryAdapter != null) {
            inventoryAdapter.updateInventoryItems(inventoryItems);
        }
    }
}
