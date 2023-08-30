/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2023 Roberto Leinardi
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

package com.leinardi.ubuntucountdownwidget.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.leinardi.ubuntucountdownwidget.ui.annotation.ThemePreviews
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing

@Composable
fun DatePickerDialog(
    show: Boolean,
    onPositiveButtonClick: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    selection: Long? = null,
    selectableDates: SelectableDates = object : SelectableDates {},
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker,
    onNegativeButtonClick: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
) {
    if (show) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selection,
            yearRange = yearRange,
            initialDisplayMode = initialDisplayMode,
            selectableDates = selectableDates,
        )
        val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }
        DatePickerDialog(
            onDismissRequest = { onDismissRequest?.invoke() },
            confirmButton = {
                TextButton(
                    onClick = { onPositiveButtonClick(datePickerState.selectedDateMillis) },
                    enabled = confirmEnabled.value,
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            modifier = modifier.padding(horizontal = Spacing.x08),
            dismissButton = {
                TextButton(onClick = { onNegativeButtonClick?.invoke() }) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

@ThemePreviews
@Composable
private fun PreviewDatePickerDialog() {
    AppTheme {
        DatePickerDialog(show = true, onPositiveButtonClick = {})
    }
}
