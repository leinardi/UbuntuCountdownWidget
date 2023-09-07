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
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidBasePlugin
import com.leinardi.ubuntucountdownwidget.ext.android
import com.leinardi.ubuntucountdownwidget.ext.config
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-android")
    id("kotlin-kapt")
    id("ubuntucountdownwidget.detekt-conventions")
    id("ubuntucountdownwidget.config-conventions")
}

val libs = the<LibrariesForLibs>()

kapt {
    useBuildCache = true
    correctErrorTypes = true
    javacOptions {
        option("-Xmaxerrs", Integer.MAX_VALUE)
    }
}

plugins.withType<AndroidBasePlugin>().configureEach {
    extensions.configure<BaseExtension> {
        compileSdkVersion(config.android.compileSdk.get())

        defaultConfig {
            minSdk = config.android.minSdk.get()
            targetSdk = config.android.targetSdk.get()

            // The following argument makes the Android Test Orchestrator run its
            // "pm clear" command after each test invocation. This command ensures
            // that the app's state is completely cleared between tests.
            setTestInstrumentationRunnerArguments(mutableMapOf("clearPackageData" to "true"))
        }

        compileOptions {
            sourceCompatibility = config.android.javaVersion.get()
            targetCompatibility = config.android.javaVersion.get()
        }

        testOptions {
            animationsDisabled = true
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }

        if (this is CommonExtension<*, *, *, *, *>) {
            lint {
                abortOnError = true
                checkAllWarnings = false
                checkDependencies = true
                checkReleaseBuilds = false
                ignoreTestSources = true
                warningsAsErrors = false
                disable.add("ResourceType")
                lintConfig = file("${project.rootDir}/config/lint/lint.xml")
            }

            kotlinOptions {
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                )
                jvmTarget = config.android.javaVersion.get().toString()
            }
        }

        packagingOptions {
            resources {
                // Use this block to exclude conflicting files that breaks your APK assemble task
                excludes.add("META-INF/LICENSE.md")
                excludes.add("META-INF/LICENSE-notice.md")
            }
        }
    }
}

fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

kotlin {
    sourceSets.all {
        languageSettings.progressiveMode =
            true // deprecations and bug fixes for unstable code take effect immediately
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = config.android.javaVersion.get().toString()
        }
    }

    withType<Test> {
        testLogging.events("skipped", "failed")
    }
}
