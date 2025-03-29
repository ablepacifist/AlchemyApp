package alchemy.srsys.application;

import android.app.Application;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        // Copy the script file from assets to the internal storage.
        copyDatabaseAssetFile();
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
    private void copyDatabaseAssetFile() {
        File filesDir = getFilesDir();
        // HSQLDB will look for a script file with the same base name as your database;
        // here, we assume your database is "game_database.db" and thus we need "game_database.script".
        File scriptFile = new File(filesDir, "game_database.script");

        if (!scriptFile.exists()) {
            AssetManager assetManager = getAssets();
            try (InputStream is = assetManager.open("db/game_database.script");
                 OutputStream os = new FileOutputStream(scriptFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

