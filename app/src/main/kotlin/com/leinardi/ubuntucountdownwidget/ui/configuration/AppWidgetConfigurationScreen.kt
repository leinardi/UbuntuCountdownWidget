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

package com.leinardi.ubuntucountdownwidget.ui.configuration

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.ext.toLocalizedDate
import com.leinardi.ubuntucountdownwidget.ui.annotation.DevicePreviews
import com.leinardi.ubuntucountdownwidget.ui.component.DatePickerDialog
import com.leinardi.ubuntucountdownwidget.ui.component.LocalNavHostController
import com.leinardi.ubuntucountdownwidget.ui.component.SettingsGroup
import com.leinardi.ubuntucountdownwidget.ui.component.SettingsMenuLink
import com.leinardi.ubuntucountdownwidget.ui.component.SettingsMenuSwitch
import com.leinardi.ubuntucountdownwidget.ui.component.placeholder
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationContract.Event
import com.leinardi.ubuntucountdownwidget.ui.configuration.AppWidgetConfigurationContract.State
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.settingsContainer
import com.leinardi.ubuntucountdownwidget.ui.theme.settingsContent
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun AppWidgetConfigurationScreen(viewModel: AppWidgetConfigurationViewModel = hiltViewModel()) {
    AppWidgetConfigurationScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Composable
fun AppWidgetConfigurationScreen(
    state: State,
    sendEvent: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.settingsContainer,
        contentColor = MaterialTheme.colorScheme.settingsContent,
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.settingsContainer),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { scaffoldPadding ->
        val dueDatePickerVisible = rememberSaveable { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = scaffoldPadding,
        ) {
            countdownItems(state, sendEvent, dueDatePickerVisible)
            launcherItems(state, sendEvent)
            infoItems(state, sendEvent)
        }
        DatePickerDialog(
            show = dueDatePickerVisible.value,
            onPositiveButtonClick = { epochInMilli ->
                epochInMilli?.let { sendEvent(Event.OnCustomDateSelected(Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate())) }
                dueDatePickerVisible.value = false
            },
            selection = state.customDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
            onNegativeButtonClick = {
                dueDatePickerVisible.value = false
            },
            onDismissRequest = {
                dueDatePickerVisible.value = false
            },
        )
    }
}

private fun LazyListScope.countdownItems(
    state: State,
    sendEvent: (event: Event) -> Unit,
    dueDatePickerVisible: MutableState<Boolean>,
) {
    item {
        SettingsGroup(
            title = { Text(stringResource(R.string.pref_countdown_options)) },
        ) {}
    }
    item(contentType = "SettingsMenuLink") {
        SettingsMenuLink(
            title = { Text(stringResource(R.string.pref_default_release_date)) },
            subtitle = { Text(text = (state.releaseDate ?: LocalDate.now()).toLocalizedDate()) },
            enabled = false,
            modifier = Modifier.placeholder(state.releaseDate == null),
        )
    }
    item(contentType = "SettingsMenuSwitch") {
        SettingsMenuSwitch(
            title = { Text(stringResource(R.string.pref_custom_date_checkbox)) },
            checked = state.useCustomDate,
            onCheckedChange = { sendEvent(Event.OnUseCustomDateChanged(it)) },
            subtitle = { Text(stringResource(R.string.pref_custom_date_checkbox_summary)) },
        )
    }
    item(contentType = "SettingsMenuLink") {
        SettingsMenuLink(
            title = { Text(stringResource(R.string.pref_custom_date)) },
            subtitle = { Text(text = state.customDate.toLocalizedDate()) },
            enabled = state.useCustomDate,
            onClick = { dueDatePickerVisible.value = true },
        )
    }
    item(contentType = "SettingsMenuSwitch") {
        SettingsMenuSwitch(
            title = { Text(stringResource(R.string.pref_use_dark_theme)) },
            checked = state.useDarkTheme,
            onCheckedChange = { sendEvent(Event.OnUseDarkThemeChanged(it)) },
            subtitle = { Text(stringResource(R.string.pref_use_dark_theme_summary)) },
        )
    }
}

private fun LazyListScope.launcherItems(
    state: State,
    sendEvent: (event: Event) -> Unit,
) {
    item {
        SettingsGroup(
            title = { Text(stringResource(R.string.pref_launcher_options)) },
        ) {}
    }
    item(contentType = "SettingsMenuSwitch") {
        SettingsMenuSwitch(
            title = { Text(stringResource(R.string.pref_show_launcher_icon)) },
            checked = state.showLauncherIcon,
            onCheckedChange = { sendEvent(Event.OnShowLauncherIconChanged(it)) },
            subtitle = { Text(stringResource(R.string.pref_show_launcher_icon_summary)) },
        )
    }
}

private fun LazyListScope.infoItems(
    state: State,
    sendEvent: (event: Event) -> Unit,
) {
    item {
        SettingsGroup(
            title = { Text(stringResource(R.string.info)) },
        ) {}
    }
    item(contentType = "SettingsMenuLink") {
        SettingsMenuLink(
            title = { Text(stringResource(R.string.pref_info_version)) },
            subtitle = { Text(text = state.version) },
            enabled = false,
        )
    }
    item(contentType = "SettingsMenuLink") {
        SettingsMenuLink(
            title = { Text(stringResource(R.string.pref_report_a_bug)) },
            subtitle = { Text(stringResource(R.string.pref_report_a_bug_summary)) },
            onClick = { sendEvent(Event.OnBugReportClicked) },
        )
    }
    item(contentType = "SettingsMenuLink") {
        val navHost = LocalNavHostController.current
        SettingsMenuLink(
            title = { Text(stringResource(R.string.open_source_licenses)) },
            onClick = { navHost.navigate(AppWidgetConfigurationDestination.OPEN_SOURCE_LICENSES.route) { launchSingleTop = true } },
        )
    }
}

@DevicePreviews
@Composable
fun PreviewAppWidgetConfigurationScreen() {
    AppTheme {
        AppWidgetConfigurationScreen(State(LocalDate.now()), {})
    }
}
