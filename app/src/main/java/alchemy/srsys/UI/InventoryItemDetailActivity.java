package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.IEffect;
import alchemy.srsys.object.IIngredient;
import alchemy.srsys.object.IKnowledgeBook;
import alchemy.srsys.object.IPotion;


public class InventoryItemDetailActivity extends AppCompatActivity {
    private TextView itemNameTextView, itemQuantityTextView, itemDescriptionTextView;
    private Button consumeButton, backButton;

    private int playerId;
    private InventoryItem inventoryItem;  // UI model passed through Intent.
    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_detail);

        // Bind UI components.
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemQuantityTextView = findViewById(R.id.itemQuantityTextView);
        itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        consumeButton = findViewById(R.id.consumeButton);
        backButton = findViewById(R.id.backButton);

        // Retrieve incoming extras.
        playerId = getIntent().getIntExtra("PLAYER_ID", -1);
        inventoryItem = (InventoryItem) getIntent().getSerializableExtra("INVENTORY_ITEM");

        // Obtain GameManager from MyApp (and access its PlayerManager as needed).
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        PlayerManager playerManager = gameManager.getPlayerManager();

        // Populate UI fields from the InventoryItem.
        if (inventoryItem != null) {
            itemNameTextView.setText(inventoryItem.getName());
            itemQuantityTextView.setText("Quantity: " + inventoryItem.getQuantity());

            // Instead of showing all master effects, get only learned effects from the player's knowledge book.
            // Retrieve the player's knowledge book.
            IKnowledgeBook kb = playerManager.getKnowledgeBook(playerId);
            // Get only the learned effects for this ingredient.
            List<IEffect> learnedEffects = kb.getEffectsForIngredient(inventoryItem.getIngredient());
            // Build the display string from the learned effects.
            StringBuilder effectsDisplay = new StringBuilder();
            for (IEffect effect : learnedEffects) {
                effectsDisplay.append(effect.getTitle()).append(", ");
            }
            if (effectsDisplay.length() > 0) {
                // Remove the trailing comma and space.
                effectsDisplay.setLength(effectsDisplay.length() - 2);
            }
            itemDescriptionTextView.setText(effectsDisplay.toString());
        }

        consumeButton.setOnClickListener(v -> {
            if ("Ingredient".equalsIgnoreCase(inventoryItem.getType())) {
                IIngredient ing = inventoryItem.getIngredient();
                playerManager.consumeIngredient(playerId, ing);
            } else if ("Potion".equalsIgnoreCase(inventoryItem.getType())) {
                IPotion pot = inventoryItem.getPotion();
                playerManager.consumePotion(playerId, pot);
            }
            Toast.makeText(InventoryItemDetailActivity.this, "Item consumed", Toast.LENGTH_SHORT).show();
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }
}


