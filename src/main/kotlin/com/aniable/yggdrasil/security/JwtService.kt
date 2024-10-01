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

package com.aniable.yggdrasil.security

import com.aniable.yggdrasil.feature.user.Users
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import kotlin.time.Duration

class JwtService(private val applicationConfig: ApplicationConfig) {

	private fun getSecretKey(): Algorithm? {
		return Algorithm.HMAC512(applicationConfig.property("jwt.secret").getString())
	}

	fun build(row: ResultRow): String {
		val now = Clock.System.now()

		val expirationDuration = applicationConfig.property("jwt.expiration").getString()
		val expiration = now.plus(Duration.parse(expirationDuration))

		return JWT.create()
			.withSubject(row[Users.id].value.toString())
			.withIssuedAt(now.toJavaInstant())
			.withNotBefore(now.toJavaInstant())
			.withExpiresAt(expiration.toJavaInstant())
			.sign(getSecretKey())
			.toString()
	}

	fun verifier(): JWTVerifier {
		return JWT.require(getSecretKey()).build()
	}
}
