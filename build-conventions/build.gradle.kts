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

// Sharing build logic between subprojects
// https://docs.gradle.org/current/samples/sample_convention_plugins.html

plugins {
    `kotlin-dsl`
    id("groovy-gradle-plugin")
}


dependencies {
    implementation(libs.plugin.aboutlibraries)
    implementation(libs.plugin.android.gradle)
    implementation(libs.plugin.appversioning)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.easylauncher)
    implementation(libs.plugin.dagger.hilt)
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.ruler)
    implementation(libs.plugin.spotless)
    implementation(libs.plugin.versions)
    implementation(libs.plugin.versions.update)
    implementation(libs.plugin.violation)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
