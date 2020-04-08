/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2020 Roberto Leinardi
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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.misc.LogUtils;
import com.leinardi.ubuntucountdownwidget.ui.dialogs.ChangelogDialogFragment;
import com.leinardi.ubuntucountdownwidget.ui.dialogs.DatePickerFragment;
import com.leinardi.ubuntucountdownwidget.ui.dialogs.OpenSourceLicensesDialogFragment;
import com.leinardi.ubuntucountdownwidget.utils.Utils;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by leinardi on 11/12/15.
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final String REPORT_A_BUG_URL = "https://github.com/leinardi/UbuntuCountdownWidget/issues";

    private SharedPreferences mPrefs;
    private Preference mCustomDatePicker;
    private CheckBoxPreference mCustomDateCheckbox;
    private EditTextPreference mUrlEditTextPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String s) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setupPreferencesScreen();
    }

    private void setupPreferencesScreen() {
        addPreferencesFromResource(R.xml.preferences);

        mCustomDateCheckbox = (CheckBoxPreference) findPreference(getString(R.string.pref_custom_date_checkbox_key));
        mCustomDatePicker = findPreference(getString(R.string.pref_custom_date_key));
        mUrlEditTextPreference = (EditTextPreference) findPreference(getString(R.string.pref_url_key));

        updateUbuntuReleaseDate();
        setCustomDatePickerClickListener();
        updateCustomUrlPreferenceState(mPrefs);
        setShowLaucherIconClickListener();
        updateAppVersion();
        setReportABugClickListener();
        setChangelogClickListener();
        setOpenSourceLicensesClickListener();

    }

    private void setOpenSourceLicensesClickListener() {
        findPreference(getString(R.string.pref_licenses_key))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        new OpenSourceLicensesDialogFragment().show(getFragmentManager(),
                                OpenSourceLicensesDialogFragment.class.getSimpleName());
                        return true;
                    }
                });
    }

    private void setChangelogClickListener() {
        findPreference(getString(R.string.pref_changelog_key))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        new ChangelogDialogFragment().show(getFragmentManager(),
                                ChangelogDialogFragment.class.getSimpleName());
                        return true;
                    }
                });
    }

    private void setReportABugClickListener() {
        findPreference(getString(R.string.pref_report_a_bug_key))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(REPORT_A_BUG_URL));
                        startActivity(browserIntent);
                        return false;
                    }
                });
    }

    private void setShowLaucherIconClickListener() {
        findPreference(getString(R.string.pref_show_launcher_icon_key))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (((CheckBoxPreference) preference).isChecked()) {
                            enableComponent(true, getActivity(),
                                    LauncherActivity.class);
                        } else {
                            enableComponent(false, getActivity(),
                                    LauncherActivity.class);
                        }
                        return true;
                    }
                });
    }

    private void updateCustomUrlPreferenceState(SharedPreferences sharedPreferences) {
        String onTouchValue = sharedPreferences.getString(getString(R.string.pref_on_touch_key),
                getString(R.string.on_touch_defaultValue));
        if ("url".equals(onTouchValue)) {
            mUrlEditTextPreference.setEnabled(true);
        } else {
            mUrlEditTextPreference.setEnabled(false);
        }
    }

    private void setCustomDatePickerClickListener() {
        mCustomDatePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), getString(R.string.pref_custom_date_key));
                return true;
            }
        });
    }

    private void updateAppVersion() {
        String version;
        try {
            PackageInfo pi = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.e(TAG, "Package name not found", e);
            version = getString(R.string.pref_info_version_error);
        }

        findPreference(getString(R.string.pref_info_version_key)).setSummary(version);
    }

    private void updateUbuntuReleaseDate() {
        DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.LONG,
                Locale.getDefault());
        dateInstance.setTimeZone(TimeZone.getTimeZone("GMT"));
        String releaseDate = dateInstance.format(Utils.getUbuntuReleaseDate().getTime());
        findPreference(getString(R.string.pref_default_release_date_key)).setSummary(releaseDate);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mCustomDateCheckbox.isChecked()) {
            mPrefs.edit().putLong(getString(R.string.pref_custom_date_key),
                    Utils.getUbuntuReleaseDate().getTimeInMillis()).commit();
        }
        // Setup the initial values
        long dateInMillis = mPrefs.getLong(getString(R.string.pref_custom_date_key),
                DatePickerFragment.DEFAULT_VALUE);
        mCustomDatePicker.setSummary(DateFormat.getDateInstance(DateFormat.LONG,
                Locale.getDefault()).format(dateInMillis));

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_custom_date_key))) {
            long dateInMillis = sharedPreferences.getLong(getString(R.string.pref_custom_date_key),
                    DatePickerFragment.DEFAULT_VALUE);
            mCustomDatePicker.setSummary(DateFormat.getDateInstance(DateFormat.LONG,
                    Locale.getDefault()).format(dateInMillis));
        } else if (key.equals(getString(R.string.pref_on_touch_key))) {
            updateCustomUrlPreferenceState(sharedPreferences);
        }
    }

    private void enableComponent(boolean enable, Context mContext, Class<?> class1) {
        ComponentName componentName = new ComponentName(mContext, class1);
        mContext.getPackageManager().setComponentEnabledSetting(
                componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
