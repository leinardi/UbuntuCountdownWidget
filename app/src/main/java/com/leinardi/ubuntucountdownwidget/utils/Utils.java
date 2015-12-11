package com.leinardi.ubuntucountdownwidget.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.webkit.WebView;

import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.misc.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {
    @SuppressWarnings("unused")
    private final static String TAG = Utils.class.getSimpleName();
    private static final Utils INSTANCE = new Utils();

    // Private constructor prevents instantiation from other classes
    private Utils(){}

    public static Utils getInstance() {
        return INSTANCE;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

  
    public float dp2px(int dip, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return dip * scale + 0.5f;
    }

    public View dialogWebView(Context context, String fileName) {
        View view = View.inflate(context, R.layout.dialog_webview, null);
        WebView web = (WebView) view.findViewById(R.id.wv_dialog);
        web.loadUrl("file:///android_asset/"+fileName);
        return view;
    }

    public CharSequence readTextFile(Context context, String fileName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = in.readLine()) != null) buffer.append(line).append('\n');
            return buffer;
        } catch (IOException e) {
            Log.e("readTextFile", "Error readind file " + fileName, e);
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
    public void donate(Context mContext) {
//        Intent intent = new Intent(mContext, DonationActivityStandalone.class);
//        mContext.startActivity(intent);
    }
    
    public void enableComponent(boolean enable, Context mContext, Class<?> class1) {
        ComponentName componentName = new ComponentName(mContext, class1);
        mContext.getPackageManager().setComponentEnabledSetting(
                componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
    }

    public GregorianCalendar getUbuntuReleseDate() {
        GregorianCalendar ubuntuReleaseDate = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        ubuntuReleaseDate.set(2014, Calendar.OCTOBER, 16, 0, 0, 0);
        
        return (GregorianCalendar)ubuntuReleaseDate.clone();
    }
}