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

import com.aniable.yggdrasil.serializer.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import java.util.*

/**
 * This is a public-facing User response object.
 * We should not include any sensitive information in this object that our users may want private (such as emails).
 *
 * For a private-facing User response object see [AuthenticatedUser].
 */
@Serializable
data class User(
	@Serializable(with = UUIDSerializer::class) val id: UUID,
	val username: String,
	val created: Instant,
) {

	constructor(row: ResultRow) : this(row[Users.id].value, row[Users.username], row[Users.created])
}
