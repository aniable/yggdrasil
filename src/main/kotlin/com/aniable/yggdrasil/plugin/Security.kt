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

import com.aniable.yggdrasil.error.ResponseStatusException
import com.aniable.yggdrasil.feature.user.UserService
import com.aniable.yggdrasil.security.AuthenticatedUserPrincipal
import com.aniable.yggdrasil.security.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.configureSecurity() {
	val jwtService by inject<JwtService>()
	val userService by inject<UserService>()

	install(Authentication) {
		jwt {
			verifier(jwtService.verifier())
			validate { jwtCredential ->
				val payload = jwtCredential.payload
				val id = UUID.fromString(payload.subject) ?: null
				val user = id?.let { userService.getUserById(it) }

				if (user == null) {
					null
				} else {
					AuthenticatedUserPrincipal(user)
				}
			}
			challenge { _, _ ->
				throw ResponseStatusException(HttpStatusCode.Unauthorized, "JWT token is not valid")
			}
		}
	}
}
