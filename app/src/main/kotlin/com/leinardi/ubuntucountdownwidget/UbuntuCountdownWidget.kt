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

package com.leinardi.ubuntucountdownwidget

import android.app.Application
import android.os.Build
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UbuntuCountdownWidget : Application() {
    override fun onCreate() {
        super.onCreate()
        configureStrictMode()
    }

    private fun configureStrictMode() {
        // This can't be initialized using `androidx.startup.Initializer` or it will cause crashes in 3rd party libs using Content Providers
        // and writing data on the main thread (e.g. LeakCanary and AndroidTestRunner).
        if (BuildConfig.DEBUG) {
            val builderThread = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitDiskReads()
                .permitCustomSlowCalls()
                .penaltyLog()
                .penaltyDeath()
                .detectResourceMismatches()
            StrictMode.setThreadPolicy(builderThread.build())

            val builderVM = StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectLeakedSqlLiteObjects()
                .detectLeakedRegistrationObjects()
                .detectFileUriExposure()
                .penaltyLog()
                .penaltyDeath()
                .detectCleartextNetwork()
                .detectContentUriWithoutPermission()
                // .detectUntaggedSockets() // https://github.com/square/okhttp/issues/3537#issuecomment-974861679
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        detectCredentialProtectedWhileLocked()
                        detectImplicitDirectBoot()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        detectIncorrectContextUse()
                        detectUnsafeIntentLaunch()
                    }
                }
            StrictMode.setVmPolicy(builderVM.build())
        }
    }
}
