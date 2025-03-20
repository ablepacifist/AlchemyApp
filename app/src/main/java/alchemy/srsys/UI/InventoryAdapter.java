package alchemy.srsys.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alchemy.srsys.R;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<InventoryItem> inventoryItems;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(InventoryItem item);
    }

    public InventoryAdapter(List<InventoryItem> inventoryItems, OnItemClickListener listener) {
        this.inventoryItems = inventoryItems;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemTypeTextView, itemQuantityTextView;

        public ViewHolder(View view) {
            super(view);
            itemNameTextView = view.findViewById(R.id.itemNameTextView);
            itemTypeTextView = view.findViewById(R.id.itemTypeTextView);
            itemQuantityTextView = view.findViewById(R.id.itemQuantityTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.itemTypeTextView.setText(item.getType());
        holder.itemQuantityTextView.setText("Quantity: " + item.getQuantity());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }
    
    public void setInventoryItems(List<InventoryItem> items) {
        this.inventoryItems = items;
    }
}
