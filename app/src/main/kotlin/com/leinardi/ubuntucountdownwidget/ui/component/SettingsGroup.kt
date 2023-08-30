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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leinardi.ubuntucountdownwidget.ui.annotation.ThemePreviews
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing

@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (title != null) {
            SettingsGroupTitle(title)
        }
        content()
    }
}

@Composable
internal fun SettingsGroupTitle(
    title: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = Spacing.x02, top = Spacing.x03, end = Spacing.x02, bottom = Spacing.x01),
        contentAlignment = Alignment.CenterStart,
    ) {
        val primary = MaterialTheme.colorScheme.primary
        val titleStyle = MaterialTheme.typography.titleSmall.copy(color = primary)
        ProvideTextStyle(value = titleStyle) { title() }
    }
}

@ThemePreviews
@Composable
private fun PreviewSettingsGroup() {
    AppTheme {
        SettingsGroup(
            title = { Text(text = "Title") },
        ) {
            Box(
                modifier = Modifier
                    .heightIn(64.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Settings group")
            }
        }
    }
}
