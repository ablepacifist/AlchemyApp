package alchemy.srsys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import alchemy.srsys.R;
import alchemy.srsys.object.IPlayerManager;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Player;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPlayerName;
    private Button buttonLogin;
    private IPlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPlayerName = findViewById(R.id.editTextPlayerName);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = editTextPlayerName.getText().toString().trim();
                if (!playerName.isEmpty()) {
                    loginPlayer(playerName);
                } else {
                    // Show error message to the user
                }
            }
        });
    }

    private void loginPlayer(String playerName) {
        // Create a new Player instance
        Player player = new Player(playerName);

        // Initialize the PlayerManager
        playerManager = new PlayerManager(player, getApplicationContext());

        // Store PlayerManager in a singleton to access in other activities
        AppDataHolder.getInstance().setPlayerManager(playerManager);

        // Start InventoryActivity
        Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
        startActivity(intent);
        finish();
    }
}
