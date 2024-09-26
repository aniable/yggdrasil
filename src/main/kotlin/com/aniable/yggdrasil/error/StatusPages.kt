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

package com.aniable.yggdrasil.error

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock

fun Application.configureStatusPages() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			val status = if (cause is ResponseStatusException) cause.status else HttpStatusCode.InternalServerError
			val error = ErrorResponse(
				statusCode = status.value,
				statusReason = status.description,
				message = cause.message,
				path = call.parameters["path"],
				timestamp = Clock.System.now()
			)
			call.respond(status, error)
		}

		status(HttpStatusCode.NotFound) { call, status ->
			val error = ErrorResponse(
				statusCode = status.value,
				statusReason = status.description,
				path = call.request.path(),
				timestamp = Clock.System.now()
			)
			call.respond(status, error)
		}
	}
}
