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

package com.aniable.yggdrasil.feature.auth

import com.aniable.yggdrasil.feature.auth.request.LoginRequest
import com.aniable.yggdrasil.feature.auth.request.RegisterRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
	configureAuthValidation()

	val service by inject<AuthService>()

	route("/auth") {
		post("/register") {
			val request = call.receive<RegisterRequest>()
			val response = service.register(request)
			call.respond(response)
		}

		post("/login") {
			val request = call.receive<LoginRequest>()
			call.respond(service.login(request))
		}
	}
}
