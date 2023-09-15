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
import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    id("ubuntucountdownwidget.android-app-conventions")
    id("ubuntucountdownwidget.app-versioning-conventions")
    alias(libs.plugins.tripletplay)
    alias(libs.plugins.kotlinx.serialization)
}

val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()
println("Release keystore ${if (useReleaseKeystore) "" else "NOT "}found!")

android {
    defaultConfig {
        applicationId = config.app.applicationId.get()
        setProperty("archivesBaseName", "ubuntucountdownwidget")

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"  // https://github.com/google/dagger/issues/2033

        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = project.properties["RELEASE_KEYSTORE_PWD"] as String
                keyAlias = "jd"
                keyPassword = project.properties["RELEASE_KEYSTORE_PWD"] as String
            }
        }
    }

    buildTypes {
        getByName("debug") {
            namespace = config.app.applicationId.get() + ".debug"
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }

        getByName("release") {
            namespace = config.app.applicationId.get()
            if (useReleaseKeystore) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                // Otherwise just use the debug keystore (this is mainly for PR CI builds)
                signingConfig = signingConfigs.getByName("debug")
            }
            isProfileable = true
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
        )
    }
}

val serviceAccountCredentialsFile: File = rootProject.file("release/play-account.json")
if (serviceAccountCredentialsFile.exists()) {
    play {
        serviceAccountCredentials.set(serviceAccountCredentialsFile)
        releaseStatus.set(if (track.get() == "internal") ReleaseStatus.COMPLETED else ReleaseStatus.DRAFT)
        defaultToAppBundles.set(true)
    }
}
println("play-account.json ${if (serviceAccountCredentialsFile.exists()) "" else "NOT "}found!")

dependencies {
    implementation(libs.aboutlibraries)
    implementation(libs.aboutlibraries.core)
    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material) // Still needed for stuff missing in M3, like ModalBottomSheetLayout
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.tooling)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.material3)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization)
    implementation(libs.material)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.timber)

    kaptAndroidTest(libs.hilt.android.compiler)

    debugImplementation(libs.androidx.compose.tooling)
}
