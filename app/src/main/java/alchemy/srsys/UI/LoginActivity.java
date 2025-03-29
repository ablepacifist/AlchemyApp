package alchemy.srsys.UI;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Player;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;

    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the player is already logged in
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int storedPlayerId = prefs.getInt("PLAYER_ID", -1);
        if (storedPlayerId != -1) {
            // Skip login and go directly to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("PLAYER_ID", storedPlayerId);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Obtain the GameManagerService instance from MyApp.
        // The boolean flag (true) indicates production; adjust as needed.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        // Extract the PlayerManager from the GameManagerService.
        playerManager = gameManager.getPlayerManager();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
        
                try {
                    // Attempt login through PlayerManager (validation moved inside the manager).
                    Player player = playerManager.loginPlayer(username, password);

                    // Save the player's ID in SharedPreferences.
                    SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                    editor.putInt("PLAYER_ID", player.getId());
                    editor.apply();

                    // Navigate to MainActivity on success.
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("PLAYER_ID", player.getId());
                    startActivity(intent);
                    finish();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to registration screen
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

