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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.leinardi.ubuntucountdownwidget.ui.preview.PreviewThemes
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing

@Composable
fun SettingsMenuSwitch(
    title: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
) {
    val alpha = if (enabled) 1f else ContentAlpha.disabled
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .toggleable(enabled = enabled, value = checked, onValueChange = onCheckedChange)
            .padding(vertical = Spacing.x01)
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SettingsTileIcon(icon = icon)
        } else {
            Spacer(modifier = Modifier.size(Spacing.x02))
        }
        SettingsTileTexts(title = title, subtitle = subtitle)
        SettingsTileAction {
            Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier.padding(end = Spacing.x01))
        }
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuSwitchOn() {
    AppTheme {
        SettingsMenuSwitch(
            checked = true,
            onCheckedChange = {},
            title = { Text(text = "Hello") },
        )
    }
}

@PreviewThemes
@Composable
private fun PreviewSettingsMenuSwitchOff() {
    AppTheme {
        SettingsMenuSwitch(
            checked = false,
            onCheckedChange = {},
            title = { Text(text = "Hello") },
            subtitle = { Text(text = "This is a longer text") },
        )
    }
}
