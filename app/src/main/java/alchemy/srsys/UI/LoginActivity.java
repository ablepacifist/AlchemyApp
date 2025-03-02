package alchemy.srsys.UI;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Player;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.application.MyApp;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize database and PlayerManager
        IStubDatabase database = ((MyApp) getApplication()).getDatabase();
        playerManager = new PlayerManager(database);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton     = findViewById(R.id.loginButton);
        registerButton  = findViewById(R.id.registerButton);

        // Set up listeners
        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> navigateToRegister());
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Ensure fields are not empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Attempt to log in
        Player player = playerManager.loginPlayer(username, password);
        if (player != null) {
            // Login successful
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Navigate to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("playerId", player.getId());
            startActivity(intent);
            finish();
        } else {
            // Login failed
            Toast.makeText(this, "Login failed. Incorrect credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToRegister() {
        // Navigate to the registration activity
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
