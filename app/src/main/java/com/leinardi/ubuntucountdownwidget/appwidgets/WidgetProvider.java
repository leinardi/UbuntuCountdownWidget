/**
 *  Ubuntu Countdown Widget
 *  Copyright (C) 2011 Roberto Leinardi
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

/*
 * TODO
 * 
 * Support for ldpi (QVGA)
 * 
 */

package com.leinardi.ubuntucountdownwidget.appwidgets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.customviews.DatePreference;
import com.leinardi.ubuntucountdownwidget.misc.Constants;
import com.leinardi.ubuntucountdownwidget.misc.Log;
import com.leinardi.ubuntucountdownwidget.ui.ConfigActivity;
import com.leinardi.ubuntucountdownwidget.utils.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Log.d(TAG, "onUpdate");
        updateWidget(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(Constants.FORCE_WIDGET_UPDATE) ||
                action.equals(Intent.ACTION_TIMEZONE_CHANGED) ||
                // action.equals(Intent.ACTION_DATE_CHANGED) ||
                action.equals(Intent.ACTION_TIME_CHANGED)) {
            // Log.d(TAG,"broadcast "+ action +" catched!");
            updateWidget(context);
        }

        super.onReceive(context, intent);
    }

    public void updateWidget(Context context) {
        ComponentName thisWidget = null;
        if (this instanceof Widget1x1Provider) {
            thisWidget = new ComponentName(context, Widget1x1Provider.class);
            // Log.d(TAG, "instanceof Widget1x1Provider");
        } else if (this instanceof Widget2x2Provider) {
            thisWidget = new ComponentName(context, Widget2x2Provider.class);
            // Log.d(TAG, "instanceof Widget2x2Provider");
        } else {
            // TODO Write a better log message
            Log.e(TAG, "instanceof ERROR !!!");
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        updateWidget(context, appWidgetManager, appWidgetIds);
    }

    public void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Log.d(TAG, "updateWidget");

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isThemeDark = mPrefs.getString(context.getString(R.string.pref_theme_key), "light")
                .equals("dark");

        GregorianCalendar today = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        GregorianCalendar ubuntuReleaseDay = Utils.getInstance().getUbuntuReleseDate();

        if (mPrefs.getBoolean(context.getString(R.string.pref_custom_date_checkbox_key), false)) {
            long ubuntuReleaseMillis = mPrefs.getLong(
                    context.getString(R.string.pref_custom_date_key), DatePreference.DEFAULT_VALUE);
            ubuntuReleaseDay.setTimeInMillis(ubuntuReleaseMillis);
        }

        setAlarmManager(context, ubuntuReleaseDay);

        long millisLeft = ubuntuReleaseDay.getTimeInMillis() - today.getTimeInMillis();
        // Only API Level 9 --> TimeUnit.MILLISECONDS.toHours(millisLeft);
        long hoursLeft = (long) Math.ceil(millisLeft / (1000 * 60 * 60.0));
        long daysLeft = (long) Math.ceil(hoursLeft / 24.0);

        RemoteViews views = null;

        if (this instanceof Widget1x1Provider) {
            views = new RemoteViews(context.getPackageName(),
                    isThemeDark ? R.layout.appwidget_1x1_dark : R.layout.appwidget_1x1_light);
            // Log.d(TAG, "instanceof Widget1x1Provider");
        } else if (this instanceof Widget2x2Provider) {
            views = new RemoteViews(context.getPackageName(),
                    isThemeDark ? R.layout.appwidget_2x2_dark : R.layout.appwidget_2x2_light);
            // Log.d(TAG, "instanceof Widget2x2Provider");
        } else {
            // TODO Write a better log message
            Log.e(TAG, "instanceof ERROR !!!");
        }

        for (int appWidgetId : appWidgetIds) {
            views.setViewVisibility(R.id.progress_bar, View.GONE);
            views.setViewVisibility(R.id.tv_footer, View.VISIBLE);

            if (millisLeft > DateUtils.DAY_IN_MILLIS / 2) {
                views.setViewVisibility(R.id.iv_header, View.VISIBLE);
                views.setViewVisibility(R.id.tv_release_big, View.GONE);
                views.setViewVisibility(R.id.iv_logo, View.GONE);
                views.setTextViewText(R.id.tv_counter, daysLeft + "");
                views.setViewVisibility(R.id.tv_counter, View.VISIBLE);
                views.setTextViewText(R.id.tv_footer, context.getString(R.string.days_left));
            } else if (millisLeft < 0) {
                String releaseNumber = String.format("%02d.%02d", ubuntuReleaseDay.getTime()
                        .getYear() - 100, ubuntuReleaseDay.getTime().getMonth() + 1);
                views.setViewVisibility(R.id.iv_header, View.VISIBLE);
                views.setViewVisibility(R.id.iv_logo, View.GONE);
                views.setViewVisibility(R.id.tv_counter, View.GONE);
                views.setTextViewText(R.id.tv_release_big, releaseNumber);
                views.setViewVisibility(R.id.tv_release_big, View.VISIBLE);
                views.setTextViewText(R.id.tv_footer, context.getString(R.string.its_here));
            } else {
                views.setViewVisibility(R.id.iv_header, View.GONE);
                views.setViewVisibility(R.id.tv_counter, View.GONE);
                views.setViewVisibility(R.id.tv_release_big, View.GONE);
                views.setViewVisibility(R.id.iv_logo, View.VISIBLE);
                views.setTextViewText(R.id.tv_footer, context.getString(R.string.coming_soon));
            }

            String strOnTouch = mPrefs.getString(context.getString(R.string.pref_on_touch_key),
                    context.getString(R.string.on_touch_defaultValue));
            Log.d(TAG, "strOnTouch=" + strOnTouch);
            Intent intent;
            if (!strOnTouch.equals("disabled")) {
                if (strOnTouch.equals("config")) {
                    intent = new Intent(context, ConfigActivity.class);
                } else {
                    String url = mPrefs.getString(context.getString(R.string.pref_url_key),
                            context.getString(R.string.url_defaultValue));
                    ;
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                }

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            } else {
                // TODO This is ugly :(
                intent = new Intent("com.leinardi.donothing");
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0 /* no requestCode */,
                    intent,
                    0 /* no flags */);
            views.setOnClickPendingIntent(R.id.rl_widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private void setAlarmManager(Context context, GregorianCalendar ubuntuReleaseDay) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
                Constants.FORCE_WIDGET_UPDATE), 0);

        GregorianCalendar now = new GregorianCalendar(TimeZone.getDefault());
        GregorianCalendar triggerCalendar = (GregorianCalendar) now.clone();

        triggerCalendar.set(Calendar.HOUR_OF_DAY, ubuntuReleaseDay.getTime().getHours());
        triggerCalendar.set(Calendar.MINUTE, ubuntuReleaseDay.getTime().getMinutes());
        triggerCalendar.set(Calendar.SECOND, ubuntuReleaseDay.getTime().getSeconds() + 1);
        triggerCalendar.set(Calendar.MILLISECOND, 0);
        if (triggerCalendar.before(now)) {
            triggerCalendar.add(Calendar.HOUR_OF_DAY, 12);
            if (triggerCalendar.before(now)) {
                triggerCalendar.add(Calendar.HOUR_OF_DAY, 12);
            }
        }

        triggerCalendar.getTimeInMillis();

        alarmManager.cancel(pi);
        alarmManager.setRepeating(AlarmManager.RTC, triggerCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_HALF_DAY, pi);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

}
