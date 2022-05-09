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

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.leinardi.ubuntucountdownwidget.R

class LauncherActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.pref_show_tutorial_key), true)
        ) {
            startConfigActivity()
            finish()
            return
        }
        setContentView(R.layout.activity_launcher)
        val web = findViewById<WebView>(R.id.wv_info)
        web.loadUrl("file:///android_asset/${getString(R.string.info_filename)}")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_launcher, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            startConfigActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun startConfigActivity() {
        Intent(this@LauncherActivity, SettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }
}
