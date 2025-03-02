package alchemy.srsys.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import alchemy.srsys.R;
import alchemy.srsys.object.IEffect;

import java.util.List;

public class KnowledgeBookAdapter extends RecyclerView.Adapter<KnowledgeBookAdapter.ViewHolder> {

    private List<KnowledgeBookFragment.KnowledgeEntry> entries;

    public KnowledgeBookAdapter(List<KnowledgeBookFragment.KnowledgeEntry> entries) {
        this.entries = entries;
    }

    @Override
    public KnowledgeBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_knowledge_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KnowledgeBookAdapter.ViewHolder holder, int position) {
        KnowledgeBookFragment.KnowledgeEntry entry = entries.get(position);
        holder.ingredientNameTextView.setText(entry.ingredientName);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView;
        public TextView knownEffectsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientNameTextView);
            knownEffectsTextView = itemView.findViewById(R.id.knownEffectsTextView);
        }
    }
}
