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

import com.leinardi.ubuntucountdownwidget.ext.AndroidConfigExt
import com.leinardi.ubuntucountdownwidget.ext.AppConfigExt
import com.leinardi.ubuntucountdownwidget.ext.ConfigExt
import com.leinardi.ubuntucountdownwidget.ext.ParamsConfigExt
import com.leinardi.ubuntucountdownwidget.ext.getOrNull

val config = extensions.create<ConfigExt>("config").apply {
    extensions.create<AndroidConfigExt>("android").apply {
        compileSdk.convention(34)
        javaVersion.convention(JavaVersion.VERSION_17)
        minSdk.convention(26)
        targetSdk.convention(34)
    }

    extensions.create<AppConfigExt>("app").apply {
        applicationId.convention("com.leinardi.ubuntucountdownwidget")
    }

    extensions.create<ParamsConfigExt>("params").apply {
        saveBuildLogToFile.convention((rootProject.extra.getOrNull("saveBuildLogToFile") as? String).toBoolean())
    }
}
