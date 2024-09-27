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

import com.aniable.yggdrasil.feature.auth.request.RegisterRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.routing.*

fun Route.configureAuthValidation() {
	install(RequestValidation) {
		validate<RegisterRequest> {
			val emailRegex = Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\$")
			val usernameRegex = Regex("^\\w+\$")

			when {
				!it.email.matches(emailRegex) -> ValidationResult.Invalid("Not a valid email")
				it.username.length !in 2..20 -> ValidationResult.Invalid("Username must be between 2 and 20 characters")
				!it.username.matches(usernameRegex) -> ValidationResult.Invalid("Username can only contain letters, numbers, and underscores")
				it.password.length !in 8..256 -> ValidationResult.Invalid("Password must be between 8 and 256 characters")
				it.password != it.confirmPassword -> ValidationResult.Invalid("Passwords must match")
				else -> ValidationResult.Valid
			}
		}
	}
}
