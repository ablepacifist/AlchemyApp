package alchemy.srsys.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import alchemy.srsys.R;
import alchemy.srsys.object.IPlayerManager;
import alchemy.srsys.object.IEffect;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KnownIngredientsActivity extends AppCompatActivity {

    private ListView listViewKnownIngredients;
    private TextView textViewNoData;
    private IPlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_known_ingredients);
        setTitle("Known Ingredients");

        listViewKnownIngredients = findViewById(R.id.listViewKnownIngredients);
        textViewNoData = findViewById(R.id.textViewNoData);

        // Retrieve PlayerManager from the AppDataHolder
        playerManager = AppDataHolder.getInstance().getPlayerManager();

        // Get known ingredients and effects
        Map<String, IEffect[]> knownIngredients = playerManager.getKnownIngredients();

        if (knownIngredients.isEmpty()) {
            // No known ingredients
            textViewNoData.setVisibility(View.VISIBLE);
            listViewKnownIngredients.setVisibility(View.GONE);
        } else {
            // Known ingredients exist
            textViewNoData.setVisibility(View.GONE);
            listViewKnownIngredients.setVisibility(View.VISIBLE);

            // Prepare data for the adapter
            List<KnownIngredientItem> items = new ArrayList<>();
            for (Map.Entry<String, IEffect[]> entry : knownIngredients.entrySet()) {
                String ingredientName = entry.getKey();
                IEffect[] effectsArray = entry.getValue();

                List<String> effectNames = new ArrayList<>();
                for (IEffect effect : effectsArray) {
                    if (effect != null) {
                        effectNames.add(effect.getTitle());
                    }
                }
                items.add(new KnownIngredientItem(ingredientName, effectNames));
            }

            // Set up the adapter
            KnownIngredientsAdapter adapter = new KnownIngredientsAdapter(this, items);
            listViewKnownIngredients.setAdapter(adapter);
        }
    }
}
