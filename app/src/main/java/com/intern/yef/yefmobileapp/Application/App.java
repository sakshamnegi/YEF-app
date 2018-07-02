package com.intern.yef.yefmobileapp.Application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //set database as persistent
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
