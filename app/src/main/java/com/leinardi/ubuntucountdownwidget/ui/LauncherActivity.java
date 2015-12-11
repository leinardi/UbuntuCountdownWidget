package com.leinardi.ubuntucountdownwidget.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.leinardi.ubuntucountdownwidget.R;

public class LauncherActivity extends Activity {

    private SharedPreferences mPrefs;
    private Button btnSettings;
    private Button btnExit;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        if(!mPrefs.getBoolean(getString(R.string.pref_show_tutorial_key), true)){
            startConfigActivity();
        }
        
        setContentView(R.layout.info);
        WebView web = (WebView) findViewById(R.id.wv_info);
        web.loadUrl("file:///android_asset/" + getString(R.string.info_filename));

        btnSettings = (Button) findViewById(R.id.btn_settings);
        btnExit = (Button) findViewById(R.id.btn_exit);


        btnSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startConfigActivity();
            }
        });

        btnExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void startConfigActivity() {
        Intent intent = new Intent(LauncherActivity.this, ConfigActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
