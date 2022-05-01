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

import android.app.Dialog
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.leinardi.ubuntucountdownwidget.R

class OpenSourceLicensesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val webView = WebView(requireContext())
        webView.loadUrl(LICENSES_PATH)
        return AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogTheme)
                .setTitle(R.string.open_source_licenses)
                .setView(webView)
                .setPositiveButton(R.string.close) { _, _ -> dismiss() }
                .create()
    }

    companion object {
        private const val LICENSES_PATH = "file:///android_asset/licenses.html"
    }
}
