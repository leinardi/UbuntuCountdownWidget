/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2023 Roberto Leinardi
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

package com.leinardi.ubuntucountdownwidget.appwidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import com.leinardi.ubuntucountdownwidget.di.AppEntryPoints
import dagger.hilt.EntryPoints
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class AppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AppWidget()

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("Widget action: ${intent.action}")
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            runBlocking {
                updateAppWidgetState(context, AppWidgetStateDefinition, GlanceAppWidgetManager(context).getGlanceIdBy(appWidgetId)) {
                    EntryPoints
                        .get(context.applicationContext, AppEntryPoints.AppInterface::class.java)
                        .getAppWidgetStateProvider()
                        .get(appWidgetId)
                }
            }
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        scheduleWidgetUpdate(context)
    }

    private fun scheduleWidgetUpdate(context: Context) {
        // Set an alarm to update widgets every x mins if the device is awake.
        // Use one-shot alarm as repeating alarms get batched while the device is asleep
        // and are then *all* delivered.
        val alarmManager: AlarmManager? = context.getSystemService()
        val nextMidnight = LocalDate.now().atTime(LocalTime.MIDNIGHT).plusDays(1).atZone(ZoneId.systemDefault()).toInstant()
        Timber.d("Scheduling the next widgets UPDATE alarm for $nextMidnight")
        alarmManager?.let { am ->
            val pendingIntent = buildUpdatePendingIntent(context)
            am.set(
                AlarmManager.RTC,
                nextMidnight.toEpochMilli(),
                pendingIntent,
            )
            Timber.d("Scheduled widget UPDATE alarm.")
        }
    }

    override fun onDisabled(context: Context) {
        // Remove the update alarm if the last widget is gone.
        val alarmManager: AlarmManager? = context.getSystemService()
        alarmManager?.let { am ->
            val pendingIntent = buildUpdatePendingIntent(context)
            am.cancel(pendingIntent)
            Timber.d("Canceled widget UPDATE alarm.")
        }
    }

    private fun getActiveWidgetIds(context: Context): IntArray {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, this::class.java)

        // return ID of all active widgets within this AppWidgetProvider
        return appWidgetManager.getAppWidgetIds(componentName)
    }

    private fun buildUpdatePendingIntent(context: Context): PendingIntent {
        val widgetClass = this::class.java
        val widgetIds = getActiveWidgetIds(context)
        val updateIntent = Intent(context, widgetClass)
            .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        val flags = PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE

        return PendingIntent.getBroadcast(context, REQUEST_CODE, updateIntent, flags)
    }

    companion object {
        private const val REQUEST_CODE = 1982
    }
}
