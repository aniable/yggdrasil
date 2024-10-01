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

package com.aniable.yggdrasil.feature.user

import com.aniable.yggdrasil.plugin.query
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.selectAll
import java.util.*

class UserService {

	suspend fun getUserFromPrincipal(principal: JWTPrincipal?): User = query {
		val subject = principal!!.subject.toString()
		Users.selectAll().where { Users.id eq UUID.fromString(subject) }.map { User(it) }.first()
	}
}
