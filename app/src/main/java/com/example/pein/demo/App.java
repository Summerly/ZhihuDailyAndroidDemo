package com.example.pein.demo;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Pein on 15/12/1.
 */
public class App extends Application {
    public static String TAG;

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
        TAG = this.getClass().getSimpleName();
        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }
}
