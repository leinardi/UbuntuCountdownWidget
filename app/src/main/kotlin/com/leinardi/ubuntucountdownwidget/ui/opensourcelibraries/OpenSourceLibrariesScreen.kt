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

package com.leinardi.ubuntucountdownwidget.ui.opensourcelibraries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.leinardi.ubuntucountdownwidget.R
import com.leinardi.ubuntucountdownwidget.ui.annotation.DevicePreviews
import com.leinardi.ubuntucountdownwidget.ui.component.LocalNavHostController
import com.leinardi.ubuntucountdownwidget.ui.theme.AppTheme
import com.leinardi.ubuntucountdownwidget.ui.theme.Spacing
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Organization
import com.mikepenz.aboutlibraries.entity.Scm
import com.mikepenz.aboutlibraries.ui.compose.HtmlText
import com.mikepenz.aboutlibraries.ui.compose.util.author
import com.mikepenz.aboutlibraries.ui.compose.util.htmlReadyLicenseContent
import com.mikepenz.aboutlibraries.util.withContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun OpenSourceLibrariesScreen() {
    val libraries = remember { mutableStateOf<Libs?>(null) }

    val context = LocalContext.current
    LaunchedEffect(libraries) {
        libraries.value = Libs.Builder().withContext(context).build()
    }

    OpenSourceLibrariesScreen(libraries = libraries.value?.libraries.orEmpty().toImmutableList())
}

@Composable
private fun OpenSourceLibrariesScreen(
    libraries: ImmutableList<Library>,
) {
    val navHost = LocalNavHostController.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.open_source_licenses)) },
                navigationIcon = {
                    IconButton(
                        onClick = { navHost.popBackStack() },
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
        ) {
            itemsIndexed(libraries) { index, library ->
                var openDialog by rememberSaveable { mutableStateOf(false) }
                LibraryItem(
                    library = library,
                    onClick = { openDialog = true },
                )
                if (index < libraries.size - 1) {
                    HorizontalDivider()
                }
                if (openDialog) {
                    val scrollState = rememberScrollState()
                    AlertDialog(
                        onDismissRequest = {
                            openDialog = false
                        },
                        confirmButton = {
                            TextButton(onClick = { openDialog = false }) {
                                Text(stringResource(R.string.close))
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier.verticalScroll(scrollState),
                            ) {
                                HtmlText(
                                    html = library.licenses.firstOrNull()?.htmlReadyLicenseContent.orEmpty(),
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                        modifier = Modifier.padding(Spacing.x02),
                        properties = DialogProperties(usePlatformDefaultWidth = false),
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(
    library: Library,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .clickable { onClick() }
                .padding(Spacing.x02),
        ) {
            val (nameRef, versionRef, authorRef, licensesRef) = createRefs()

            Text(
                text = library.name,
                modifier = Modifier
                    .constrainAs(nameRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(versionRef.start)
                        width = Dimension.fillToConstraints
                    },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = library.artifactVersion.orEmpty(),
                modifier = Modifier.constrainAs(versionRef) {
                    end.linkTo(parent.end)
                    baseline.linkTo(nameRef.baseline)
                    visibility = library.artifactVersion.orGone()
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = library.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                modifier = Modifier
                    .constrainAs(authorRef) {
                        end.linkTo(parent.end)
                        top.linkTo(versionRef.bottom, Spacing.half)
                        visibility = library.author.orGone()
                    },
            )

            FlowRow(
                modifier = Modifier
                    .constrainAs(licensesRef) {
                        start.linkTo(parent.start)
                        top.linkTo(nameRef.bottom, Spacing.half)
                        end.linkTo(authorRef.start)
                        width = Dimension.fillToConstraints
                        visibility = library.licenses.orGone()
                    },
                horizontalArrangement = Arrangement.spacedBy(Spacing.x01),
                verticalArrangement = Arrangement.spacedBy(Spacing.half),
            ) {
                library.licenses.forEach {
                    Badge(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary,
                    ) {
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PreviewOpenSourceLibrariesScreen() {
    val lib = Library(
        uniqueId = "",
        artifactVersion = "1.0.0",
        name = "Library name",
        description = "",
        website = "",
        developers = emptyList(),
        organization = Organization("Author", ""),
        scm = Scm("", "", ""),
        licenses = setOf(
            License("License", "", "", hash = ""),
        ),
    )
    AppTheme {
        OpenSourceLibrariesScreen(List(10) { lib }.toImmutableList())
    }
}

private fun String?.orGone() = if (isNullOrEmpty()) Visibility.Gone else Visibility.Visible
private fun Collection<*>?.orGone() = if (this.isNullOrEmpty()) Visibility.Gone else Visibility.Visible
