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
import se.bjurr.violations.comments.github.plugin.gradle.ViolationCommentsToGitHubTask

plugins {
    id("se.bjurr.violations.violation-comments-to-github-gradle-plugin")
}

tasks.register<ViolationCommentsToGitHubTask>("violationCommentsToGitHub") {
    notCompatibleWithConfigurationCache("https://github.com/tomasbjerre/violation-comments-to-github-gradle-plugin/issues/13")
    setRepositoryOwner("leinardi")
    setRepositoryName("ubuntucountdownwidget")
    setPullRequestId(System.getProperties()["GITHUB_PULLREQUESTID"] as? String)
    setoAuth2Token(System.getProperties()["GITHUB_OAUTH2TOKEN"] as? String)
    setGitHubUrl("https://api.github.com/")
    setCreateCommentWithAllSingleFileComments(false)
    setCreateSingleFileComments(true)
    setCommentOnlyChangedContent(true)
    setKeepOldComments(false)
    setViolations(
        listOf(
            listOf(
                "KOTLINGRADLE",
                ".",
                ".*/build/logs/buildlog.*\\.txt\$",
                "Gradle",
            ),
            listOf(
                "CHECKSTYLE",
                ".",
                ".*/reports/detekt/.*\\.xml\$",
                "Detekt",
            ),
            listOf(
                "ANDROIDLINT",
                ".",
                ".*/reports/lint-results.*\\.xml\$",
                "Android Lint",
            ),
            listOf(
                "JUNIT",
                ".",
                ".*/build/test-results/test.*/.*\\.xml\$",
                "JUnit",
            ),
            listOf(
                "JUNIT",
                ".",
                ".*/build/sauce/saucectl-report\\.xml\$",
                "Espresso",
            ),
        ),
    )
}
