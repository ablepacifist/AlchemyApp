package alchemy.srsys.application;

import android.app.Application;

import alchemy.srsys.object.IStubDatabase;
import alchemy.srsys.data.StubDatabase;

public class MyApp extends Application {
    private IStubDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new StubDatabase();
    }

    public IStubDatabase getDatabase() {
        return database;
    }
}
