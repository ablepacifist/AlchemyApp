package alchemy.srsys.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import alchemy.srsys.R;
import alchemy.srsys.object.IEffect;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<IngredientsFragment.IngredientEntry> entries;

    public IngredientsAdapter(List<IngredientsFragment.IngredientEntry> entries) {
        this.entries = entries;
    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {
        IngredientsFragment.IngredientEntry entry = entries.get(position);
        holder.ingredientNameTextView.setText(entry.ingredientName);

        // Display the quantity
        holder.quantityTextView.setText("Quantity: " + entry.quantity);

        // Build a string of known effects
        StringBuilder effectsBuilder = new StringBuilder();
        for (IEffect effect : entry.knownEffects) {
            if (effect != null) {
                effectsBuilder.append(effect.getTitle()).append(", ");
            }
        }
        // Remove trailing comma if needed
        if (effectsBuilder.length() > 0) {
            effectsBuilder.setLength(effectsBuilder.length() - 2);
        } else {
            effectsBuilder.append("No known effects");
        }

        holder.knownEffectsTextView.setText(effectsBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView;
        public TextView quantityTextView;
        public TextView knownEffectsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            knownEffectsTextView = itemView.findViewById(R.id.knownEffectsTextView);
        }
    }
}
