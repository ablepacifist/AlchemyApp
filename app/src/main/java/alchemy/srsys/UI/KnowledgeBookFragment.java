package alchemy.srsys.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alchemy.srsys.R;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KnowledgeBookFragment extends Fragment {

    private Player player;
    private GameManager gameManager;
    private RecyclerView recyclerView;
    private KnowledgeBookAdapter knowledgeBookAdapter;

    public KnowledgeBookFragment(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_book, container, false);

        recyclerView = view.findViewById(R.id.knowledgeBookRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get knowledge book data from the player's knowledge book
        List<KnowledgeEntry> knowledgeEntries = getKnowledgeEntries();

        // Set up adapter
        knowledgeBookAdapter = new KnowledgeBookAdapter(knowledgeEntries);
        recyclerView.setAdapter(knowledgeBookAdapter);

        return view;
    }

    private List<KnowledgeEntry> getKnowledgeEntries() {
        List<KnowledgeEntry> entries = new ArrayList<>();

        // Retrieve the player's knowledge book
        Map<String, List<IEffect>> knowledgeBook = gameManager.getPlayerKnowledgeBookEntries(player);

        // For each ingredient, get its known effects
        for (Map.Entry<String, List<IEffect>> entry : knowledgeBook.entrySet()) {
            String ingredientName = entry.getKey();
            List<IEffect> knownEffects = entry.getValue();

            entries.add(new KnowledgeEntry(ingredientName, knownEffects));
        }

        return entries;
    }

    // Inner class to represent a knowledge entry
    static class KnowledgeEntry {
        String ingredientName;
        List<IEffect> knownEffects;

        KnowledgeEntry(String ingredientName, List<IEffect> knownEffects) {
            this.ingredientName = ingredientName;
            this.knownEffects = knownEffects;
        }
    }
}
