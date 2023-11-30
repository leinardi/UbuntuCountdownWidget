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

import com.leinardi.ubuntucountdownwidget.ext.config
import com.leinardi.ubuntucountdownwidget.ext.params
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.logging.LoggingOutputInternal
import java.text.SimpleDateFormat
import java.util.Date

if (config.params.saveBuildLogToFile.get()) {
    val datetime = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date())
    val buildLogDir = layout.buildDirectory.dir("logs")
    mkdir(buildLogDir)
    val buildLog = buildLogDir.get().file("buildlog-${datetime}.txt").asFile
    System.setProperty("org.gradle.color.error", "RED")

    val outputListener = StandardOutputListener { output -> buildLog.appendText(output.toString()) }
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardOutputListener(outputListener)
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardErrorListener(outputListener)
}
