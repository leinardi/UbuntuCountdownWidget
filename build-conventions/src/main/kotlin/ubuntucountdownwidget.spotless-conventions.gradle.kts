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
import com.diffplug.gradle.spotless.SpotlessTask
import com.diffplug.spotless.LineEnding
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.diffplug.spotless")
}

val libs = the<LibrariesForLibs>()

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        diktat(libs.versions.diktat.get()).configFile("$rootDir/config/diktat/diktat-analysis.yml")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    format("graphql") {
        target("**/*.graphql")
        prettier(libs.versions.prettier.get()).configFile("$rootDir/config/prettier/prettierrc-graphql.yml")
    }

    format("yml") {
        target("**/*.yml", "**/*.yaml")
        prettier(libs.versions.prettier.get()).configFile("$rootDir/config/prettier/prettierrc-yml.yml")
    }

    format("androidXml") {
        target("**/AndroidManifest.xml", "src/**/*.xml")
        targetExclude("**/mergedManifests/**/AndroidManifest.xml", "**/build/**/*.xml")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }

    format("misc") {
        // define the files to apply `misc` to
        target("**/*.md", "**/.gitignore")

        // define the steps to apply to those files
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks {
    withType<SpotlessTask> {
        mustRunAfter(":app:copyMergedManifests")
    }
}
