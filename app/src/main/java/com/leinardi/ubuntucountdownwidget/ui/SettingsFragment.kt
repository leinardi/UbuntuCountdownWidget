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
package com.leinardi.ubuntucountdownwidget.ui

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.ui.dialogs.ChangelogDialogFragment
import com.leinardi.ubuntucountdownwidget.ui.dialogs.DatePickerFragment
import com.leinardi.ubuntucountdownwidget.ui.dialogs.OpenSourceLicensesDialogFragment
import com.leinardi.ubuntucountdownwidget.utils.Utils
import timber.log.Timber
import java.text.DateFormat
import java.util.Locale
import java.util.TimeZone

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(activity) }
    private val customDatePicker: Preference by lazy {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_custom_date_key)))
    }
    private val customDateCheckbox: CheckBoxPreference by lazy {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_custom_date_checkbox_key)))
                as CheckBoxPreference
    }
    private val urlEditTextPreference: EditTextPreference by lazy {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_url_key))) as EditTextPreference
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        setupPreferencesScreen()
    }

    override fun onResume() {
        super.onResume()
        if (!customDateCheckbox.isChecked) {
            prefs.edit().putLong(getString(R.string.pref_custom_date_key),
                    Utils.ubuntuReleaseDate.timeInMillis).apply()
        }
        // Setup the initial values
        val dateInMillis = prefs.getLong(getString(R.string.pref_custom_date_key),
                DatePickerFragment.DEFAULT_VALUE)
        customDatePicker.summary = DateFormat.getDateInstance(DateFormat.LONG,
                Locale.getDefault()).format(dateInMillis)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_custom_date_key)) {
            val dateInMillis = sharedPreferences.getLong(getString(R.string.pref_custom_date_key),
                    DatePickerFragment.DEFAULT_VALUE)
            customDatePicker.summary = DateFormat.getDateInstance(DateFormat.LONG,
                    Locale.getDefault()).format(dateInMillis)
        } else if (key == getString(R.string.pref_on_touch_key)) {
            updateCustomUrlPreferenceState(sharedPreferences)
        }
    }

    private fun setupPreferencesScreen() {
        addPreferencesFromResource(R.xml.preferences)
        updateUbuntuReleaseDate()
        setCustomDatePickerClickListener()
        updateCustomUrlPreferenceState(prefs)
        setShowLaucherIconClickListener()
        updateAppVersion()
        setReportABugClickListener()
        setChangelogClickListener()
        setOpenSourceLicensesClickListener()
    }

    private fun setOpenSourceLicensesClickListener() {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_licenses_key))).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    OpenSourceLicensesDialogFragment().show(checkNotNull(fragmentManager),
                            OpenSourceLicensesDialogFragment::class.java.simpleName)
                    true
                }
    }

    private fun setChangelogClickListener() {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_changelog_key))).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    ChangelogDialogFragment().show(checkNotNull(fragmentManager),
                            ChangelogDialogFragment::class.java.simpleName)
                    true
                }
    }

    private fun setReportABugClickListener() {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_report_a_bug_key))).onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(REPORT_A_BUG_URL))
                    startActivity(browserIntent)
                    false
                }
    }

    private fun setShowLaucherIconClickListener() {
        checkNotNull(findPreference<Preference>(getString(R.string.pref_show_launcher_icon_key)))
                .onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
            if ((preference as CheckBoxPreference).isChecked) {
                enableComponent(true, LauncherActivity::class.java)
            } else {
                enableComponent(false, LauncherActivity::class.java)
            }
            true
        }
    }

    private fun updateCustomUrlPreferenceState(sharedPreferences: SharedPreferences) {
        val onTouchValue = sharedPreferences.getString(getString(R.string.pref_on_touch_key),
                getString(R.string.on_touch_defaultValue))
        urlEditTextPreference.isEnabled = "url" == onTouchValue
    }

    private fun setCustomDatePickerClickListener() {
        customDatePicker.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(checkNotNull(fragmentManager), getString(R.string.pref_custom_date_key))
            true
        }
    }

    private fun updateAppVersion() {
        val version: String
        version = try {
            val pi = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            pi.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Package name not found")
            getString(R.string.pref_info_version_error)
        }
        checkNotNull(findPreference<Preference>(getString(R.string.pref_info_version_key))).summary = version
    }

    private fun updateUbuntuReleaseDate() {
        val dateInstance = DateFormat.getDateInstance(DateFormat.LONG,
                Locale.getDefault())
        dateInstance.timeZone = TimeZone.getTimeZone("GMT")
        val releaseDate = dateInstance.format(Utils.ubuntuReleaseDate.time)
        checkNotNull(findPreference<Preference>(getString(R.string.pref_default_release_date_key)))
                .summary = releaseDate
    }

    private fun enableComponent(enable: Boolean, class1: Class<*>) {
        val componentName = ComponentName(requireContext(), class1)
        requireContext().packageManager.setComponentEnabledSetting(
                componentName,
                if (enable) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                },
                PackageManager.DONT_KILL_APP)
    }

    companion object {
        private const val REPORT_A_BUG_URL = "https://github.com/leinardi/UbuntuCountdownWidget/issues"
    }
}
