/**
 *  Ubuntu Countdown Widget
 *  Copyright (C) 2011 Roberto Leinardi
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 */

package com.leinardi.ubuntucountdownwidget.misc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.leinardi.ubuntucountdownwidget.R;
import com.leinardi.ubuntucountdownwidget.utils.Utils;

/**
 * Displays an EULA ("End User License Agreement") that the user has to accept
 * before using the application. Your application should call
 * {@link com.leinardi.ubuntucountdownwidget.misc.Eula#show(android.app.Activity)} in the onCreate() method of the first
 * activity. If the user accepts the EULA, it will never be shown again. If the
 * user refuses, {@link android.app.Activity#finish()} is invoked on your
 * activity.
 */
public class Eula {
	/**
	 * callback to let the activity know when the user has accepted the EULA.
	 */
	static interface OnEulaAgreedTo {

		/**
		 * Called when the user has accepted the eula and the dialog closes.
		 */
		void onEulaAgreedTo();
	}

	/**
	 * Displays the EULA if necessary. This method should be called from the
	 * onCreate() method of your main Activity.
	 * 
	 * @param activity
	 *            The Activity to finish if the user rejects the EULA.
	 * @return Whether the user has agreed already.
	 */
	public static boolean show(final Activity activity) {
		final SharedPreferences preferences = activity.getSharedPreferences(Constants.PREF_EULA, Activity.MODE_PRIVATE);
		// preferences.edit().putBoolean(Constants.PREF_EULA_ACCEPTED, false).commit();
		if (!preferences.getBoolean(Constants.PREF_EULA_ACCEPTED, false)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle(R.string.eula_title);
			builder.setCancelable(true);
			builder.setPositiveButton(R.string.eula_accept, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					accept(preferences);
					if (activity instanceof OnEulaAgreedTo) {
						((OnEulaAgreedTo) activity).onEulaAgreedTo();
					}
				}
			});
			builder.setNegativeButton(R.string.eula_refuse, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					refuse(activity);
				}
			});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					refuse(activity);
				}
			});
			builder.setView(Utils.getInstance().dialogWebView(activity, activity.getString(R.string.eula_filename)));
			builder.create().show();
			return false;
		}
		return true;
	}

	private static void accept(SharedPreferences preferences) {
		preferences.edit().putBoolean(Constants.PREF_EULA_ACCEPTED, true).commit();
	}

	private static void refuse(Activity activity) {
		activity.finish();
	}
}
