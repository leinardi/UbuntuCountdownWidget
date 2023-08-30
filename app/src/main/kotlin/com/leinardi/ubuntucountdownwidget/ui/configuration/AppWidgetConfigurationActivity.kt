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

package com.leinardi.ubuntucountdownwidget.ui.configuration

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidget
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidgetStateDefinition
import com.leinardi.ubuntucountdownwidget.appwidget.AppWidgetStateProvider
import com.leinardi.ubuntucountdownwidget.ui.component.LocalNavHostController
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppWidgetConfigurationActivity :
    AppCompatActivity() {  // AppCompatActivity is needed to be able to toggle Day/Night programmatically
    @Inject lateinit var appWidgetStateProvider: AppWidgetStateProvider

    @Inject lateinit var glanceAppWidgetManager: GlanceAppWidgetManager
    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appWidgetId = getAppWidgetId()

        if (setWidgetConfiguredResult()) {
            return
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme(dynamicColor = true) {
                val navHostController: NavHostController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavHostController provides navHostController,
                ) {
                    NavHost(navController = navHostController, startDestination = AppWidgetConfigurationDestination.MAIN.route) {
                        AppWidgetConfigurationDestination.values().forEach { category ->
                            composable(route = category.route) {
                                category.composable(this, it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setWidgetConfiguredResult()
    }

    private fun setWidgetConfiguredResult(): Boolean {
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            setResult(RESULT_CANCELED)
            finish()
            return true
        }

        // Update the app widgets
        updateWidgets()

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        return false
    }

    private fun getAppWidgetId() = intent?.extras?.getInt(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID,
    ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

    private fun updateWidgets() {
        lifecycleScope.launch {
            glanceAppWidgetManager.getGlanceIds(AppWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(
                    context = this@AppWidgetConfigurationActivity,
                    definition = AppWidgetStateDefinition,
                    glanceId = glanceId,
                ) { appWidgetStateProvider.get(glanceAppWidgetManager.getAppWidgetId(glanceId)) }
            }
            AppWidget().updateAll(this@AppWidgetConfigurationActivity.applicationContext)
        }
    }
}
