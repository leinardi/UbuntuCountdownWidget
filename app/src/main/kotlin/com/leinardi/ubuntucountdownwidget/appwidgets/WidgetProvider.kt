/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2022 Roberto Leinardi
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.leinardi.ubuntucountdownwidget.appwidgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.view.View
import android.widget.RemoteViews
import androidx.preference.PreferenceManager
import com.leinardi.ubuntucountdownwidget.BuildConfig
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.ui.SettingsActivity
import com.leinardi.ubuntucountdownwidget.ui.dialogs.DatePickerFragment
import com.leinardi.ubuntucountdownwidget.utils.Utils
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil

@Suppress("MagicNumber")
abstract class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        updateWidget(context)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == FORCE_WIDGET_UPDATE ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED
        ) {
            updateWidget(context)
        }
        super.onReceive(context, intent)
    }

    private fun updateWidget(context: Context) {
        val thisWidget = getComponentName(context)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
        updateWidget(context, appWidgetManager, appWidgetIds)
    }

    @Suppress("NestedBlockDepth")
    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val isThemeDark =
            prefs.getString(context.getString(R.string.pref_theme_key), "light") == "dark"
        val today = GregorianCalendar(TimeZone.getTimeZone("GMT"))
        val ubuntuReleaseDay = Utils.ubuntuReleaseDate
        if (prefs.getBoolean(context.getString(R.string.pref_custom_date_checkbox_key), false)) {
            val ubuntuReleaseMillis = prefs.getLong(
                context.getString(R.string.pref_custom_date_key),
                DatePickerFragment.DEFAULT_VALUE,
            )
            ubuntuReleaseDay.timeInMillis = ubuntuReleaseMillis
        }
        setAlarmManager(context, ubuntuReleaseDay)
        val millisLeft = ubuntuReleaseDay.timeInMillis - today.timeInMillis
        // Only API Level 9 --> TimeUnit.MILLISECONDS.toHours(millisLeft);
        val hoursLeft = ceil(millisLeft / (1000 * 60 * 60.0)).toLong()
        val daysLeft = ceil(hoursLeft / 24.0).toLong()
        val views = getRemoteViews(context, isThemeDark)
        for (appWidgetId in appWidgetIds) {
            setupViews(context, ubuntuReleaseDay, millisLeft, daysLeft, views)
            val strOnTouch = prefs.getString(
                context.getString(R.string.pref_on_touch_key),
                context.getString(R.string.on_touch_defaultValue),
            )
            var intent = Intent()
            if (strOnTouch != "disabled") {
                if (strOnTouch == "config") {
                    intent = Intent(context, SettingsActivity::class.java)
                } else {
                    var url = checkNotNull(
                        prefs.getString(
                            context.getString(R.string.pref_url_key),
                            context.getString(R.string.url_defaultValue),
                        ),
                    )
                    if (!url.toLowerCase(Locale.ROOT).matches("^\\w+://.*".toRegex())) {
                        url = "http://$url"
                    }
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                }
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,  // no requestCode
                intent,
                FLAG_IMMUTABLE,
            )
            views.setOnClickPendingIntent(R.id.rl_widget, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    protected abstract fun getComponentName(context: Context): ComponentName

    protected abstract fun getRemoteViews(context: Context, isThemeDark: Boolean): RemoteViews

    private fun setupViews(
        context: Context,
        ubuntuReleaseDay: GregorianCalendar,
        millisLeft: Long,
        daysLeft: Long,
        views: RemoteViews,
    ) {
        views.setViewVisibility(R.id.progress_bar, View.GONE)
        views.setViewVisibility(R.id.footer_text_view, View.VISIBLE)
        when {
            millisLeft > DateUtils.DAY_IN_MILLIS / 2 -> {
                views.setViewVisibility(R.id.header_image_view, View.VISIBLE)
                views.setViewVisibility(R.id.release_text_view, View.GONE)
                views.setViewVisibility(R.id.logo_image_view, View.GONE)
                views.setTextViewText(R.id.counter_text_view, daysLeft.toString())
                views.setViewVisibility(R.id.counter_text_view, View.VISIBLE)
                views.setTextViewText(R.id.footer_text_view, context.getString(R.string.days_left))
            }

            millisLeft < 0 -> {
                val releaseNumber = String.format(
                    Locale.ROOT,
                    "%02d.%02d",
                    ubuntuReleaseDay[Calendar.YEAR] - 2000,
                    ubuntuReleaseDay[Calendar.MONTH] + 1,
                )
                views.setViewVisibility(R.id.header_image_view, View.VISIBLE)
                views.setViewVisibility(R.id.logo_image_view, View.GONE)
                views.setViewVisibility(R.id.counter_text_view, View.GONE)
                views.setTextViewText(R.id.release_text_view, releaseNumber)
                views.setViewVisibility(R.id.release_text_view, View.VISIBLE)
                views.setTextViewText(R.id.footer_text_view, context.getString(R.string.its_here))
            }

            else -> {
                views.setViewVisibility(R.id.header_image_view, View.GONE)
                views.setViewVisibility(R.id.counter_text_view, View.GONE)
                views.setViewVisibility(R.id.release_text_view, View.GONE)
                views.setViewVisibility(R.id.logo_image_view, View.VISIBLE)
                views.setTextViewText(
                    R.id.footer_text_view,
                    context.getString(R.string.coming_soon),
                )
            }
        }
    }

    private fun setAlarmManager(context: Context, ubuntuReleaseDay: GregorianCalendar) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context,
            0,
            Intent(FORCE_WIDGET_UPDATE),
            FLAG_IMMUTABLE,
        )
        val now = GregorianCalendar(TimeZone.getDefault())
        val triggerCalendar = now.clone() as GregorianCalendar
        triggerCalendar[Calendar.HOUR_OF_DAY] = ubuntuReleaseDay[Calendar.HOUR]
        triggerCalendar[Calendar.MINUTE] = ubuntuReleaseDay[Calendar.MINUTE]
        triggerCalendar[Calendar.SECOND] = ubuntuReleaseDay[Calendar.SECOND] + 1
        triggerCalendar[Calendar.MILLISECOND] = 0
        if (triggerCalendar.before(now)) {
            triggerCalendar.add(Calendar.HOUR_OF_DAY, 12)
            if (triggerCalendar.before(now)) {
                triggerCalendar.add(Calendar.HOUR_OF_DAY, 12)
            }
        }
        triggerCalendar.timeInMillis
        alarmManager.cancel(pi)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            triggerCalendar.timeInMillis,
            AlarmManager.INTERVAL_HALF_DAY,
            pi,
        )
    }

    companion object {
        const val FORCE_WIDGET_UPDATE = BuildConfig.APPLICATION_ID + ".FORCE_WIDGET_UPDATE"
    }
}
