package com.gci.gestioncapteursincendie;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class FirebaseActivity extends Application {

    private static FirebaseActivity mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        try {
            FirebaseApp.initializeApp(this);
        }
        catch (Exception e) {
        }
    }

    public static Context getInstance() {
        return mInstance;
    }
}