package com.master.univt.support;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.master.univt.R;

import java.util.HashMap;


/**
 * Created by LQG on 2014/12/4.
 */
public class GlobalApplication extends Application {


    private static final String PROPERTY_ID = "UA-58418434-1";
    static GlobalApplication instance;

    public static GlobalApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }


    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     *
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                    : analytics.newTracker(R.xml.global_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public void trackView(final String screenName) {
        // Get tracker.
        Log.d("LOG", "Send screen");
        Tracker t = ((GlobalApplication) this).getTracker(
                TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());

    }

    /**
     * Tracks event with action and label.
     *
     * @param category the category of the event
     * @param action the action of the event
     * @param label the label for the event
     */
    public void trackEvent(final String category, final String action, final String label)
    {
        Tracker t = ((GlobalApplication) this).getTracker(
                TrackerName.APP_TRACKER);
//        t.send(new HitBuilders.createEvent(category, action, label, null).build());
    }

}
