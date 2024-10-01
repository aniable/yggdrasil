/*
 * Yggdrasil
 * Copyright (C) 2024 Aniable LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.aniable.yggdrasil.plugin

import com.aniable.yggdrasil.error.configureStatusPages
import com.aniable.yggdrasil.feature.auth.authRoutes
import com.aniable.yggdrasil.feature.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	configureStatusPages()

	routing {
		route("/api") {
			v1Routes()
		}
	}
}

fun Route.v1Routes() {
	route("/v1") {
		authRoutes()
		userRoutes()
	}
}
