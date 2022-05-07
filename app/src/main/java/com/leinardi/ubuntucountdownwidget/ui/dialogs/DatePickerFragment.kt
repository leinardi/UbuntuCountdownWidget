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

package com.leinardi.ubuntucountdownwidget.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.utils.Utils
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            requireContext(),
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val calendar = getPersistedCalendar(DEFAULT_VALUE)
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar[year, month, day, 0, 0] = 0
        persistLong(calendar.timeInMillis)
    }

    private fun getPersistedLong(defaultReturnValue: Long) =
        sharedPreferences.getLong(getString(R.string.pref_custom_date_key), defaultReturnValue)

    private fun persistLong(value: Long): Boolean {
        if (value == getPersistedLong(value.inv())) {
            // It's already there, so the same as persisting
            return true
        }
        val editor = sharedPreferences.edit()
        editor.putLong(getString(R.string.pref_custom_date_key), value)
        tryCommit(editor)
        return true
    }

    private fun tryCommit(editor: SharedPreferences.Editor) {
        try {
            editor.apply()
        } catch (unused: AbstractMethodError) {
            // The app injected its own pre-Gingerbread
            // SharedPreferences.Editor implementation without
            // an apply method.
            editor.commit()
        }
    }

    private fun getPersistedCalendar(defaultValue: Long) = Calendar.getInstance().apply {
        timeInMillis = getPersistedLong(defaultValue)
    }

    companion object {
        @JvmField
        val DEFAULT_VALUE = Utils.ubuntuReleaseDate.timeInMillis
    }
}
