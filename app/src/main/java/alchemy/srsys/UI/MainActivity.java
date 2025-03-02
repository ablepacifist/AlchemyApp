package alchemy.srsys.UI;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import alchemy.srsys.R;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.object.Player;
import alchemy.srsys.application.MyApp;

public class MainActivity extends AppCompatActivity {

    private Button inventoryButton;
    private Button knowledgeBookButton;
    private Button brewButton;

    private GameManager gameManager;
    private Player player;

    // Fragments
    private InventoryFragment inventoryFragment;
    private KnowledgeBookFragment knowledgeBookFragment;
    private BrewFragment brewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get player ID passed from LoginActivity
        int playerId = getIntent().getIntExtra("playerId", -1);
        if (playerId == -1) {
            // Handle error (e.g., finish the activity)
            finish();
            return;
        }

        // Initialize database and GameManager
        IStubDatabase database = ((MyApp) getApplication()).getDatabase();
        gameManager = new GameManager(database);
        player = gameManager.getPlayerById(playerId);

        // Initialize UI components
        inventoryButton = findViewById(R.id.inventoryButton);
        knowledgeBookButton = findViewById(R.id.knowledgeBookButton);
        brewButton = findViewById(R.id.brewButton);

        // Set up fragments
        inventoryFragment = new InventoryFragment(player, gameManager);
        knowledgeBookFragment = new KnowledgeBookFragment(player, gameManager);
        brewFragment = new BrewFragment(player, gameManager);

        // Set default fragment
        setFragment(inventoryFragment);
        updateButtonStyles(inventoryButton);

        // Set button click listeners
        inventoryButton.setOnClickListener(v -> onInventoryButtonClicked());
        knowledgeBookButton.setOnClickListener(v -> onKnowledgeBookButtonClicked());
        brewButton.setOnClickListener(v -> onBrewButtonClicked());
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    private void onInventoryButtonClicked() {
        setFragment(inventoryFragment);
        updateButtonStyles(inventoryButton);
    }

    private void onKnowledgeBookButtonClicked() {
        setFragment(knowledgeBookFragment);
        updateButtonStyles(knowledgeBookButton);
    }

    private void onBrewButtonClicked() {
        setFragment(brewFragment);
        updateButtonStyles(brewButton);
    }

    private void updateButtonStyles(Button activeButton) {
        // Reset styles for all buttons
        inventoryButton.setEnabled(true);
        knowledgeBookButton.setEnabled(true);
        brewButton.setEnabled(true);

        // Disable the active button to indicate it's selected
        activeButton.setEnabled(false);
    }

    public void refreshInventoryFragment() {
        if (inventoryFragment != null && inventoryFragment.isAdded()) {
            inventoryFragment.refreshInventory();
        }
    }
}

