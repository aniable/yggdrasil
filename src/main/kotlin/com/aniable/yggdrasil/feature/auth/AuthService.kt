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

import com.aniable.yggdrasil.error.ResponseStatusException
import com.aniable.yggdrasil.feature.auth.request.LoginRequest
import com.aniable.yggdrasil.feature.auth.request.RegisterRequest
import com.aniable.yggdrasil.feature.user.User
import com.aniable.yggdrasil.feature.user.Users
import com.aniable.yggdrasil.plugin.query
import com.aniable.yggdrasil.security.JwtService
import com.password4j.Password
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class AuthService(private val jwtService: JwtService) {

	suspend fun register(request: RegisterRequest): User = query {
		val normalizedEmail = request.email.lowercase()
		val normalizedUsername = request.username.lowercase()
		val hashedPassword =
			Password.hash(request.password).addRandomSalt().withArgon2().result ?: throw ResponseStatusException(
				HttpStatusCode.InternalServerError, "Could not hash password"
			)

		if (Users.selectAll().where { Users.email eq normalizedEmail }.count() > 0) {
			throw ResponseStatusException(HttpStatusCode.Conflict, "User with email already exists")
		}

		if (Users.selectAll().where { Users.username eq normalizedUsername }.count() > 0) {
			throw ResponseStatusException(HttpStatusCode.Conflict, "User with username already exists")
		}

		val user = Users.insert {
			it[email] = normalizedEmail
			it[username] = normalizedUsername
			it[password] = hashedPassword
		}.resultedValues?.firstOrNull() ?: throw ResponseStatusException(
			HttpStatusCode.InternalServerError, "Could not create new user"
		)

		User(user)
	}

	suspend fun login(request: LoginRequest): Map<String, String> = query {
		val foundUser = Users.selectAll().where { Users.email eq request.email.lowercase() }.firstOrNull()
			?: throw ResponseStatusException(
				HttpStatusCode.Unauthorized, "Bad credentials"
			)

		val matches = Password.check(request.password, foundUser[Users.password]).withArgon2()
		if (!matches) throw ResponseStatusException(HttpStatusCode.Unauthorized, "Bad credentials")

		val jwt = jwtService.build(foundUser)
		mapOf("token" to jwt)
	}
}
