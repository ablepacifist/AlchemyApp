package alchemy.srsys.UI;

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
import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.IKnowledgeBook;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class KnowledgeBookFragment extends Fragment {
    private static final String ARG_PLAYER_ID = "playerId";
    private int playerId;
    private RecyclerView recyclerView;
    private KnowledgeBookAdapter adapter;

    public static KnowledgeBookFragment newInstance(int playerId) {
        KnowledgeBookFragment fragment = new KnowledgeBookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLAYER_ID, playerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            playerId = getArguments().getInt(ARG_PLAYER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledge_book, container, false);
        recyclerView = view.findViewById(R.id.knowledgeBookRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the knowledge book from the logic layer.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        PlayerManager playerManager = gameManager.getPlayerManager();
        IKnowledgeBook kb = playerManager.getKnowledgeBook(playerId);

        // Map the knowledge book to UI models using the updated method.
        IStubDatabase db = MyApp.getDatabase(true);
        List<KnowledgeEntry> entries = UIDataMapper.toKnowledgeEntryList(kb, db);

        // Set up the adapter and display the entries.
        adapter = new KnowledgeBookAdapter(entries);
        recyclerView.setAdapter(adapter);
        return view;
    }

}


