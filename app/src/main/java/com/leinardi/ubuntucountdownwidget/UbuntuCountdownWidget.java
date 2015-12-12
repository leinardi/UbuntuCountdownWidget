package com.leinardi.ubuntucountdownwidget;

import android.app.Application;

/**
 * Created by leinardi on 31/05/14.
 */
public class UbuntuCountdownWidget extends Application {

    private static UbuntuCountdownWidget mSingleton;

    public static UbuntuCountdownWidget getInstance() {
        return mSingleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSingleton = this;

        if (!BuildConfig.DEBUG) {
            FabricWrapper.with(this);
        }
    }

}
