package com.leinardi.ubuntucountdownwidget.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.leinardi.ubuntucountdownwidget.R;

public class LauncherActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!mPrefs.getBoolean(getString(R.string.pref_show_tutorial_key), true)) {
            startConfigActivity();
            return;
        }

        setContentView(R.layout.activity_launcher);

        WebView web = (WebView) findViewById(R.id.wv_info);
        web.loadUrl("file:///android_asset/" + getString(R.string.info_filename));

    }

    private void startConfigActivity() {
        Intent intent = new Intent(LauncherActivity.this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startConfigActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
