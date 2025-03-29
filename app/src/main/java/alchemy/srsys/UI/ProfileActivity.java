package alchemy.srsys.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.Player;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;


public class ProfileActivity extends AppCompatActivity {

    private TextView playerNameTextView, playerLevelTextView;
    private Button logoutButton, levelUpButton;
    private int playerId;
    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Bind UI components.
        playerNameTextView = findViewById(R.id.playerNameTextView);
        playerLevelTextView = findViewById(R.id.playerLevelTextView);
        logoutButton = findViewById(R.id.logoutButton);
        levelUpButton = findViewById(R.id.levelUpButton);

        // Retrieve the player's ID passed via the Intent.
        playerId = getIntent().getIntExtra("PLAYER_ID", -1);

        // Obtain the PlayerManager via GameManagerService.
        GameManagerService gameManager = MyApp.getGameManagerService(true);
        playerManager = gameManager.getPlayerManager();

        // Get the Player object and display name and level.
        Player player = playerManager.getPlayerById(playerId);
        if (player != null) {
            playerNameTextView.setText(player.getUsername());
            playerLevelTextView.setText("Level: " + player.getLevel());
        } else {
            Toast.makeText(this, "Player not found", Toast.LENGTH_SHORT).show();
        }

        // Set up the Level Up button.
        levelUpButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("Level Up Confirmation");

            final EditText input = new EditText(ProfileActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String enteredPassword = input.getText().toString();
                    try {
                        // Now delegate the level-up logic (including password verification) to the PlayerManager.
                        if (playerManager.levelUpPlayer(player, enteredPassword)) {
                            Toast.makeText(ProfileActivity.this,
                                    "Congratulations! New level: " + player.getLevel(),
                                    Toast.LENGTH_SHORT).show();
                            playerLevelTextView.setText("Level: " + player.getLevel());
                        } else {
                            Toast.makeText(ProfileActivity.this,
                                    "You have reached the maximum level (10)!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (IllegalArgumentException e) {
                        // If the password is incorrect, the PlayerManager throws an exception.
                        Toast.makeText(ProfileActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
            builder.show();
        });



        // Set up the Logout button.
        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
            editor.remove("PLAYER_ID");
            editor.apply();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
