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

package com.leinardi.ubuntucountdownwidget.ui

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.appwidgets.Widget1x1Provider
import com.leinardi.ubuntucountdownwidget.appwidgets.Widget2x2Provider
import com.leinardi.ubuntucountdownwidget.appwidgets.WidgetProvider

class SettingsActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
        // Log.d(TAG, "onBackPressed");
        resultIntent()
        finish()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (AppWidgetManager.ACTION_APPWIDGET_CONFIGURE == intent.action) {
            val snackbar = Snackbar.make(
                findViewById(R.id.main_content),
                R.string.press_save_to_add_the_widget,
                Snackbar.LENGTH_INDEFINITE,
            )
            snackbar.setAction(R.string.close) { snackbar.dismiss() }
            snackbar.show()
        } else {
            menu.findItem(R.id.action_save).apply {
                isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == R.id.action_save) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onPause() {
        super.onPause()
        resultIntent()
    }

    private fun resultIntent() {
        if (CONFIGURE_ACTION == intent.action) {
            val intent = intent
            val extras = intent.extras
            if (extras != null) {
                val appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID,
                )
                val result = Intent()
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                setResult(Activity.RESULT_OK, result)
            }
        }
        sendBroadcast(
            Intent(this, Widget1x1Provider::class.java).apply {
                action = WidgetProvider.FORCE_WIDGET_UPDATE
            },
        )
        sendBroadcast(
            Intent(this, Widget2x2Provider::class.java).apply {
                action = WidgetProvider.FORCE_WIDGET_UPDATE
            },
        )
    }

    companion object {
        private const val CONFIGURE_ACTION = "android.appwidget.action.APPWIDGET_CONFIGURE"
    }
}
