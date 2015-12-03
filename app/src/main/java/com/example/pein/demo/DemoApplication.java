package com.example.pein.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Pein on 15/12/1.
 */
public class DemoApplication extends Application {
    public static String TAG;

    private static DemoApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        TAG = this.getClass().getSimpleName();
        mInstance = this;
    }

    public static synchronized DemoApplication getInstance() {
        return mInstance;
    }
}
