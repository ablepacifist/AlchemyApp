package alchemy.srsys.UI;

import android.app.Application;
import android.content.Context;

import java.io.File;

import alchemy.srsys.application.MyApp;

public class Alchemy extends Application {
    private static Alchemy instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApp.getDatabase(true);
        MyApp.getGameManagerService(true);
        //MyApp.getUserManagerInstance(true);
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
    public static String getDatabasePath() {
        File databaseFile = new File(getContext().getFilesDir(), "game_database.db");
        return databaseFile.getAbsolutePath();
    }
}