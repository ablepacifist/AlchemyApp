package alchemy.srsys.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import alchemy.srsys.R;

import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private List<InventoryItem> inventoryItems;

    public InventoryAdapter(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    @Override
    public InventoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryAdapter.ViewHolder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.nameTextView.setText(item.name);
        holder.typeTextView.setText(item.type);
        holder.quantityTextView.setText("Quantity: " + item.quantity);
    }
    public void updateInventoryItems(List<InventoryItem> newItems) {
        this.inventoryItems.clear();
        this.inventoryItems.addAll(newItems);
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView typeTextView;
        public TextView quantityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.itemNameTextView);
            typeTextView = itemView.findViewById(R.id.itemTypeTextView);
            quantityTextView = itemView.findViewById(R.id.itemQuantityTextView);
        }
    }
}

