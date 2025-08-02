package alchemy.srsys.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.R;
import alchemy.srsys.application.MyApp;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;
import alchemy.srsys.object.DTO.AuthResponse;
import alchemy.srsys.object.DTO.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alchemy.srsys.application.ApiClient;

import alchemy.srsys.object.Player;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button  loginButton;
    private Button  registerButton;

    // Switch to false to use offline stub
    private final boolean isProduction = true;
    private PlayerManager playerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton      = findViewById(R.id.loginButton);
        registerButton   = findViewById(R.id.registerButton);

        if (!isProduction) {
            GameManagerService gm = MyApp.getGameManagerService(false);
            playerManager = gm.getPlayerManager();
        }

        loginButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString().trim();
            String pass = passwordEditText.getText().toString();

            if (isProduction) {
                performNetworkLogin(user, pass);
            } else {
                fallbackLogin(user, pass);
            }
        });

        registerButton.setOnClickListener(
                v -> startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void performNetworkLogin(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);

        ApiClient.getService()
                .login(request)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call,
                                           Response<AuthResponse> response) {
                        Log.d(TAG, "onResponse: HTTP " + response.code());
                        if (response.isSuccessful() && response.body() != null) {
                            int playerId = response.body().getPlayerId();
                            saveAndLaunch(playerId);
                        } else {
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Login failed: " + response.code(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Log.e(TAG, "onFailure: network error", t);
                        Toast.makeText(
                                LoginActivity.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void fallbackLogin(String username, String password) {
        try {
            Player p = playerManager.loginPlayer(username, password);
            saveAndLaunch(p.getId());
        } catch (IllegalArgumentException e) {
            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void saveAndLaunch(int playerId) {
        getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .edit()
                .putInt("PLAYER_ID", playerId)
                .apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PLAYER_ID", playerId);
        startActivity(intent);
        finish();
    }
}
