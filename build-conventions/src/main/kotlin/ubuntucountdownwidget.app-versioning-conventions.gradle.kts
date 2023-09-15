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

import io.github.reactivecircus.appversioning.SemVer
import java.time.Instant
import kotlin.math.log10
import kotlin.math.pow

plugins {
    id("io.github.reactivecircus.app-versioning")
}

appVersioning {
    overrideVersionCode { gitTag, _, _ ->
        val semVer = SemVer.fromGitTag(gitTag)
        val version = semVer.major * 10000 + semVer.minor * 100 + semVer.patch
        val versionLength = (log10(version.toDouble()) + 1).toInt()
        var epoch = Instant.now().epochSecond.toInt()
        epoch -= epoch % 10.0.pow(versionLength.toDouble()).toInt()
        version + epoch
    }
    overrideVersionName { gitTag, _, variantInfo ->
        "${gitTag.rawTagName}${if (variantInfo.buildType == "debug") "-dev" else ""}"
    }
}
