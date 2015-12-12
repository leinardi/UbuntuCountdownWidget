package com.leinardi.ubuntucountdownwidget.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.widget.DatePicker;

import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.utils.Utils;

import java.util.Calendar;

/**
 * Created by leinardi on 11/12/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final long DEFAULT_VALUE = Utils.getInstance().getUbuntuReleseDate().getTimeInMillis();
    private SharedPreferences mSharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        // Use the current date as the default date in the picker
        final Calendar c = this.getPersitendCalendar(DEFAULT_VALUE);
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

    private Calendar getPersitendCalendar(final long defaultValue) {
        final long date = this.getPersistedLong(defaultValue);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c;
    }
}