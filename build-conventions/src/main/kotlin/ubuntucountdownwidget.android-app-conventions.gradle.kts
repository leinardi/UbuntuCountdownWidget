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
import com.mikepenz.aboutlibraries.plugin.DuplicateMode
import com.mikepenz.aboutlibraries.plugin.DuplicateRule
import com.project.starter.easylauncher.filter.ChromeLikeFilter
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.android.application")
    id("ubuntucountdownwidget.android-conventions")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.mikepenz.aboutlibraries.plugin")
    id("ubuntucountdownwidget.merged-manifests-conventions")
    id("ubuntucountdownwidget.dependencies-conventions")
    id("com.starter.easylauncher")
    id("ubuntucountdownwidget.ruler-conventions")
}
val libs = the<LibrariesForLibs>()

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled =
            true  // https://developer.android.com/studio/write/java8-support#library-desugaring
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.ui.test.ExperimentalTestApi",
        )
    }
}

easylauncher {
    buildTypes {
        create("debug") {
            filters(
                chromeLike(
                    gravity = ChromeLikeFilter.Gravity.TOP,
                    label = "DEBUG",
                    textSizeRatio = 0.20f,
                    labelPadding = 10,
                ),
            )
        }
    }
}

aboutLibraries {
    duplicationMode = DuplicateMode.MERGE
    duplicationRule = DuplicateRule.SIMPLE
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    debugImplementation(libs.leakcanary)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlin.result)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)
    kapt(libs.dagger.hilt.compiler)

    androidTestUtil(libs.androidx.test.orchestrator)
}
