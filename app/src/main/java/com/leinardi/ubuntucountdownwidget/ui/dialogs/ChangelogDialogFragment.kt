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
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.leinardi.ubuntucountdownwidget.R

class ChangelogDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val changelogView = View.inflate(activity, R.layout.dialog_changelog, null)
        return AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogTheme)
                .setTitle(R.string.changelog_title)
                .setView(changelogView)
                .setPositiveButton(R.string.close) { _, _ -> dismiss() }
                .create()
    }
}
