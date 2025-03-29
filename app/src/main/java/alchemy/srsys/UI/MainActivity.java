package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
 

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button inventoryButton, knowledgeBookButton, brewButton, forageButton, profileButton;
    private FrameLayout fragmentContainer;
    private GameManagerService gameManager;
    private int playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the logged-in player's ID.
        playerId = getIntent().getIntExtra("PLAYER_ID", -1);
        // Get only the GameManagerService – no direct use of PlayerManager here.
        gameManager = MyApp.getGameManagerService(true);

        // Bind UI elements.
        profileButton = findViewById(R.id.profileButton);
        inventoryButton = findViewById(R.id.inventoryButton);
        knowledgeBookButton = findViewById(R.id.knowledgeBookButton);
        brewButton = findViewById(R.id.brewButton);
        forageButton = findViewById(R.id.forageButton);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        // Set click listener for the Profile button.
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("PLAYER_ID", playerId);
            startActivity(intent);
        });
        // Set up click listeners delegating actions via GameManagerService.
        inventoryButton.setOnClickListener(view ->
                loadFragment(InventoryFragment.newInstance(playerId)));

        knowledgeBookButton.setOnClickListener(view ->
                loadFragment(KnowledgeBookFragment.newInstance(playerId)));

        brewButton.setOnClickListener(view ->
                loadFragment(BrewFragment.newInstance(playerId)));

        forageButton.setOnClickListener(view -> {
            // Create a confirmation dialog
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirm Forage")
                    .setMessage("Are you sure you want to forage?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Proceed with the forage action
                        String ingredientName = gameManager.forage(playerId);
                        Toast.makeText(MainActivity.this, "Foraged new ingredient: " + ingredientName, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Do nothing and dismiss the dialog
                        dialog.dismiss();
                    })
                    .show();
        });


        // Load a default fragment (e.g., InventoryFragment) on startup.
        loadFragment(InventoryFragment.newInstance(playerId));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

