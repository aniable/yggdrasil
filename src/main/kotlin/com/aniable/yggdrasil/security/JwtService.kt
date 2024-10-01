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
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.ktor.server.config.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.ResultRow
import java.util.*
import javax.crypto.SecretKey
import kotlin.time.Duration

class JwtService(private val applicationConfig: ApplicationConfig) {

	private fun getSecretKey(): SecretKey? {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationConfig.property("jwt.secret").getString()))
	}

	private fun extractAllClaims(token: String): Claims {
		return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).payload
	}

	private fun <T> extractClaim(token: String, resolver: (Claims) -> T): T {
		val claims = extractAllClaims(token)
		return resolver(claims)
	}

	fun extractSubject(token: String): String {
		return extractClaim(token, Claims::getSubject)
	}

	fun build(row: ResultRow): String {
		val now = Clock.System.now()
		val nowDate = Date.from(now.toJavaInstant())

		val expirationDuration = applicationConfig.property("jwt.expiration").getString()
		val expiration = now.plus(Duration.parse(expirationDuration))
		val expirationDate = Date.from(expiration.toJavaInstant())

		return Jwts.builder()
			.subject(row[Users.id].value.toString())
			.issuedAt(nowDate)
			.notBefore(nowDate)
			.expiration(expirationDate)
			.signWith(getSecretKey())
			.compact()
	}
}
