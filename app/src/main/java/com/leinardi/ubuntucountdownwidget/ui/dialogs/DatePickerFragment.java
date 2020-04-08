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

package com.leinardi.ubuntucountdownwidget.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;
import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.utils.Utils;

import java.util.Calendar;

/**
 * Created by leinardi on 11/12/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final long DEFAULT_VALUE = Utils.getUbuntuReleaseDate().getTimeInMillis();
    private SharedPreferences mSharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Use the current date as the default date in the picker
        final Calendar c = this.getPersistedCalendar(DEFAULT_VALUE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0, 0);
        persistLong(c.getTimeInMillis());
    }

    protected long getPersistedLong(long defaultReturnValue) {
        return mSharedPreferences.getLong(getString(R.string.pref_custom_date_key), defaultReturnValue);
    }

    protected boolean persistLong(long value) {
        if (value == getPersistedLong(~value)) {
            // It's already there, so the same as persisting
            return true;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(getString(R.string.pref_custom_date_key), value);
        tryCommit(editor);
        return true;
    }

    private void tryCommit(SharedPreferences.Editor editor) {
        try {
            editor.apply();
        } catch (AbstractMethodError unused) {
            // The app injected its own pre-Gingerbread
            // SharedPreferences.Editor implementation without
            // an apply method.
            editor.commit();
        }
    }

    private Calendar getPersistedCalendar(final long defaultValue) {
        final long date = this.getPersistedLong(defaultValue);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c;
    }
}
