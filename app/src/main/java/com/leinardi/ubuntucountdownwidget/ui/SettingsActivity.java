/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2015 Roberto Leinardi
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

package com.leinardi.ubuntucountdownwidget.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.appwidgets.WidgetProvider;

public class SettingsActivity extends AppCompatActivity {
    private static final String CONFIGURE_ACTION = "android.appwidget.action.APPWIDGET_CONFIGURE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onPause() {
        super.onPause();
        resultIntent();
    }

    @Override
    public void onBackPressed() {
        // This will be called either automatically for you on 2.0
        // or later, or by the code above on earlier versions of the
        // platform.
        // Log.d(TAG, "onBackPressed");
        resultIntent();
        finish();
    }

    private void resultIntent() {
        if (CONFIGURE_ACTION.equals(getIntent().getAction())) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                Intent result = new Intent();

                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, result);
            }
        }
        sendBroadcast(new Intent(WidgetProvider.FORCE_WIDGET_UPDATE));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (AppWidgetManager.ACTION_APPWIDGET_CONFIGURE.equals(getIntent().getAction())) {
            final Snackbar snackbar = Snackbar.make(findViewById(R.id.main_content), R.string.press_save_to_add_the_widget, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.close, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            MenuItem item = menu.findItem(R.id.action_save);
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
