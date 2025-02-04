package alchemy.srsys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import alchemy.srsys.R;
import alchemy.srsys.object.IPlayerManager;
import alchemy.srsys.object.IIngredient;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private Button buttonKnownIngredients;
    private ListView listViewInventory;
    private IPlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        setTitle("Inventory");

        buttonKnownIngredients = findViewById(R.id.buttonKnownIngredients);
        listViewInventory = findViewById(R.id.listViewInventory);

        // Retrieve PlayerManager from AppDataHolder
        playerManager = AppDataHolder.getInstance().getPlayerManager();

        // Populate the inventory list
        List<IIngredient> inventory = playerManager.getInventory();
        InventoryAdapter adapter = new InventoryAdapter(this, inventory);
        listViewInventory.setAdapter(adapter);

        // Set up button listener
        buttonKnownIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, KnownIngredientsActivity.class);
                startActivity(intent);
            }
        });
    }
}
