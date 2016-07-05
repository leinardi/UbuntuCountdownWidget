/*
 * Ubuntu Countdown Widget
 * Copyright (C) 2015 Roberto Leinardi
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

package com.leinardi.ubuntucountdownwidget;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by leinardi on 31/05/14.
 */
public class UbuntuCountdownWidget extends Application {

    private static UbuntuCountdownWidget mSingleton;

    public static UbuntuCountdownWidget getInstance() {
        return mSingleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mSingleton = this;

        if (!BuildConfig.DEBUG) {
            FabricWrapper.with(this);
        }
    }

}
