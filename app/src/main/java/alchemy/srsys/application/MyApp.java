package alchemy.srsys.application;

import android.app.Application;

import java.io.File;
import java.sql.SQLException;

//import alchemy.srsys.UI.Alchemy;
import alchemy.srsys.data.HSQLDatabase;
import alchemy.srsys.data.IStubDatabase;
import alchemy.srsys.data.StubDatabase;
import alchemy.srsys.logic.GameManager;
import alchemy.srsys.logic.GameManagerService;
import alchemy.srsys.logic.PlayerManager;


public class MyApp extends Application {
    private static MyApp instance;
    private static IStubDatabase database;
    private static final boolean IS_PRODUCTION = true;

    private static GameManagerService gameManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Initialize the database and game manager using this context.
        getDatabase(IS_PRODUCTION);
        getGameManagerService(IS_PRODUCTION);
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static GameManagerService getGameManagerService(boolean isProduction) {
        if (gameManager == null) {
            gameManager = GameManager.getInstance(isProduction);
        }
        return gameManager;
    }

    public static IStubDatabase getDatabase(boolean isProduction) {
        if (database == null) {
            if (isProduction) {
                // Use MyApp's context instead of Alchemy
                String databasePath = getDatabasePath();
                try {
                    database = new HSQLDatabase(databasePath);
                } catch (SQLException e) {
                    System.err.println("Failed to initialize HSQLDatabase: " + e.getMessage());
                }
            } else {
                database = StubDatabase.getInstance();
            }
        }
        return database;
    }

    public static String getDatabasePath() {
        File databaseFile = new File(getInstance().getFilesDir(), "game_database.db");
        return databaseFile.getAbsolutePath();
    }
}

