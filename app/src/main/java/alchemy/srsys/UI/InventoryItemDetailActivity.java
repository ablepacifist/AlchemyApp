package alchemy.srsys.UI;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
        TextView itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        itemDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);  // 24sp text size.

        // Bind UI components.
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemQuantityTextView = findViewById(R.id.itemQuantityTextView);
        itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        consumeButton = findViewById(R.id.consumeButton);
        backButton = findViewById(R.id.backButton);

        // Retrieve incoming extras.
        playerId = getIntent().getIntExtra("PLAYER_ID", -1);
        inventoryItem = (InventoryItem) getIntent().getSerializableExtra("INVENTORY_ITEM");

        // Obtain GameManagerService and then PlayerManager.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        playerManager = gameManager.getPlayerManager();

        // Populate UI fields based on the item type.
        if (inventoryItem != null) {
            itemNameTextView.setText(inventoryItem.getName());
            itemQuantityTextView.setText("Quantity: " + inventoryItem.getQuantity());

            // Check if the item is an Ingredient or a Potion.
            if ("Ingredient".equalsIgnoreCase(inventoryItem.getType()) && inventoryItem.getIngredient() != null) {
                // For ingredients, fetch learned effects from the player's knowledge book.
                IKnowledgeBook kb = playerManager.getKnowledgeBook(playerId);
                List<IEffect> learnedEffects = kb.getEffectsForIngredient(inventoryItem.getIngredient());
                StringBuilder sb = new StringBuilder();
                for (IEffect effect : learnedEffects) {
                    sb.append(effect.getTitle()).append(", ");
                }
                if (sb.length() > 0) {
                    // Remove the trailing comma and space.
                    sb.setLength(sb.length() - 2);
                }
                itemDescriptionTextView.setText(sb.toString());
                itemDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);  // Set to 24sp for ingredients.
            } else // For potions, build a formatted string using individual getters.
                if ("Potion".equalsIgnoreCase(inventoryItem.getType()) && inventoryItem.getPotion() != null) {
                    IPotion potion = inventoryItem.getPotion();
                    StringBuilder sb = new StringBuilder();

                    // Display basic information.
                    sb.append("Name: ").append(potion.getName()).append("\n");
                    sb.append("Brew Level: ").append(potion.getBrewLevel()).append("\n");
                    sb.append("Duration: ").append(potion.getDuration()).append(" minute(s)\n");
                    sb.append("Bonus Dice: ").append(potion.getDice()).append("\n\n");

                    // Ingredients section.
                    sb.append("Ingredients:\n");
                    if (potion.getIngredient1() != null) {
                        sb.append("  - ").append(potion.getIngredient1().getName()).append("\n");
                    }
                    if (potion.getIngredient2() != null) {
                        sb.append("  - ").append(potion.getIngredient2().getName()).append("\n");
                    }
                    sb.append("\n");

                    // Effects section.
                    sb.append("Effects:\n");
                    for (IEffect effect : potion.getEffects()) {
                        sb.append("  • ").append(effect.getTitle())
                                .append(": ").append(effect.getDescription()).append("\n");
                    }

                    itemDescriptionTextView.setText(sb.toString());
                }
                else {
                // If no valid type, clear the description.
                Log.d("no valid type", "no valid type");
                itemDescriptionTextView.setText("");
            }
        }

        // Set click listener on the consume button.
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

        // Set click listener for the back button.
        backButton.setOnClickListener(v -> finish());
    }
}
