package com.hema.mypetslover;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by hema on 12/10/2017.
 */

public class MyApplication extends Application {

    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public void startTracking() {
        if (sTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            sTracker = ga.newTracker(R.xml.track_app);
            ga.enableAutoActivityReports(this);
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }

    }

    public Tracker getTracker() {
        startTracking();
        return sTracker;
    }


}