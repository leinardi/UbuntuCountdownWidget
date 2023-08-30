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

package com.leinardi.ubuntucountdownwidget.interactor

import com.leinardi.ubuntucountdownwidget.interactor.GetUseCustomDateInteractor.Companion.USE_CUSTOM_DATE_PREF_KEY
import com.leinardi.ubuntucountdownwidget.repository.DataStoreRepository
import javax.inject.Inject

class StoreUseCustomDateInteractor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(enabled: Boolean) {
        dataStoreRepository.storeValue(USE_CUSTOM_DATE_PREF_KEY, enabled)
    }
}
