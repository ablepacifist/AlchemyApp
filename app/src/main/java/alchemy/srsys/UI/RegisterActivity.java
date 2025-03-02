package alchemy.srsys.UI;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.application.MyApp;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;

    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database and PlayerManager
        IStubDatabase database = ((MyApp) getApplication()).getDatabase();
        playerManager = new PlayerManager(database);

        // Initialize UI components
        usernameEditText       = findViewById(R.id.usernameEditText);
        passwordEditText       = findViewById(R.id.passwordEditText);
        confirmPasswordEditText= findViewById(R.id.confirmPasswordEditText);
        registerButton         = findViewById(R.id.registerButton);

        // Set up listener
        registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String username        = usernameEditText.getText().toString().trim();
        String password        = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Ensure fields are not empty
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Attempt to register
        boolean success = playerManager.registerPlayer(username, password);
        if (success) {
            // Registration successful
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish(); // Go back to login screen
        } else {
            // Registration failed
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }
}
