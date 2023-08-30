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
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.util.Locale

afterEvaluate {
    val outputPath = "$projectDir/versions/dependencies"
    mkdir(outputPath)
    val compileDependencyReportTask = tasks.register("generateRuntimeDependenciesReport") {
        description = "Generates a text file containing the Runtime classpath dependencies."
    }
    project.configurations.filter { it.name.contains("RuntimeClasspath") }.forEach { configuration ->
        val configurationTask = tasks.register(
            "generate${configuration.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}DependenciesReport",
            DependencyReportTask::class.java,
        ) {
            configurations = setOf(configuration)
            outputFile = File("$outputPath/${configuration.name}Dependencies.txt")
        }
        compileDependencyReportTask.configure { dependsOn(configurationTask) }
    }
    tasks.named("check").dependsOn(tasks.named("generateRuntimeDependenciesReport"))
}
