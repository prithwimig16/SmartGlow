package com.example.bhargav.myapplication;

import com.telink.TelinkApplication;

/**
 * Created by Shanmuka on 5/3/2018.
 */
public class SmartGlowApplication extends TelinkApplication {

    private static SmartGlowApplication mThis;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static SmartGlowApplication getInstance() {
        if (mThis == null) {
            mThis = new SmartGlowApplication();
        }

        return mThis;
    }

    public boolean isServiceStarted(){
        return serviceStarted;
    }

    public boolean isServiceConnected(){
        return serviceConnected;
    }
}
