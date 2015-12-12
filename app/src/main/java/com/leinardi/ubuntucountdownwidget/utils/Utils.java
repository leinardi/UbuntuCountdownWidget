package com.leinardi.ubuntucountdownwidget.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {
    @SuppressWarnings("unused")
    private final static String TAG = Utils.class.getSimpleName();
    private static final Utils INSTANCE = new Utils();

    // Private constructor prevents instantiation from other classes
    private Utils() {
    }

    public static Utils getInstance() {
        return INSTANCE;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void enableComponent(boolean enable, Context mContext, Class<?> class1) {
        ComponentName componentName = new ComponentName(mContext, class1);
        mContext.getPackageManager().setComponentEnabledSetting(
                componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public GregorianCalendar getUbuntuReleseDate() {
        GregorianCalendar ubuntuReleaseDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        ubuntuReleaseDate.set(2014, Calendar.OCTOBER, 16, 0, 0, 0);

        return (GregorianCalendar) ubuntuReleaseDate.clone();
    }
}