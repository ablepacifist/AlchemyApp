package alchemy.srsys.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;


public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;

    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Obtain GameManagerService directly from MyApp.
        // The boolean flag (true) indicates production; adjust as needed.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        // Extract the PlayerManager from the game manager.
        playerManager = gameManager.getPlayerManager();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirm = confirmPasswordEditText.getText().toString();
        
                try {
                    // Register the player through PlayerManager (it now validates password matching internally).
                    playerManager.registerPlayer(username, password, confirm);
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish(); // Return to the previous (e.g., login) screen.
                } catch (IllegalArgumentException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }
}
