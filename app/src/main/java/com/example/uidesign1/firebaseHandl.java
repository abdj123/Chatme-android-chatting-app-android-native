package com.example.uidesign1;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class firebaseHandl extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
