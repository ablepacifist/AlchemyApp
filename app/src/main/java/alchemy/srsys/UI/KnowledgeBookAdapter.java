package alchemy.srsys.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alchemy.srsys.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KnowledgeBookAdapter extends RecyclerView.Adapter<KnowledgeBookAdapter.ViewHolder> {
    private List<KnowledgeEntry> knowledgeEntries;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientNameTextView, knownEffectsTextView;

        public ViewHolder(View view) {
            super(view);
            ingredientNameTextView = view.findViewById(R.id.ingredientNameTextView);
            knownEffectsTextView = view.findViewById(R.id.knownEffectsTextView);
        }
    }

    public KnowledgeBookAdapter(List<KnowledgeEntry> knowledgeEntries) {
        this.knowledgeEntries = knowledgeEntries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_knowledge_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        KnowledgeEntry entry = knowledgeEntries.get(position);
        holder.ingredientNameTextView.setText(entry.getIngredientName());
        holder.knownEffectsTextView.setText(entry.getKnownEffects());
    }

    @Override
    public int getItemCount() {
        return knowledgeEntries.size();
    }
}

