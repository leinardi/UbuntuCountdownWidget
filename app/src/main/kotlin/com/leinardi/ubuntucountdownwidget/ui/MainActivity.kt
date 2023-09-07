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

package com.leinardi.ubuntucountdownwidget.ui

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidget
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidgetReceiver
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidgetStateProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var appWidgetStateProvider: AppWidgetStateProvider

    @Inject lateinit var glanceAppWidgetManager: GlanceAppWidgetManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            glanceAppWidgetManager.requestPinGlanceAppWidget(
                receiver = AppWidgetReceiver::class.java,
                preview = AppWidget(),
                previewState = appWidgetStateProvider.get(AppWidgetManager.INVALID_APPWIDGET_ID),
                successCallback = null,
            )
            finish()
        }
    }

    override fun onDestroy() {
        // Workaround to prevent executing a deep link again on activity recreated (e.g. theme change)
        if (intent.action == Intent.ACTION_VIEW) {
            intent.data = null
        }
        super.onDestroy()
    }
}
