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
import io.ktor.server.response.*
import kotlinx.datetime.Clock

private fun buildErrorResponse(call: ApplicationCall, status: HttpStatusCode, cause: Throwable? = null): ErrorResponse {
	return ErrorResponse(
		statusCode = status.value,
		statusReason = status.description,
		message = cause?.message ?: cause?.stackTraceToString(),
		path = call.parameters["path"],
		timestamp = Clock.System.now()
	)
}

fun Application.configureStatusPages() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			val status = HttpStatusCode.InternalServerError
			val error = buildErrorResponse(call, status, cause)
			call.respond(status, error)
		}

		exception<ResponseStatusException> { call, cause ->
			val status = cause.status
			val error = buildErrorResponse(call, status, cause)
			call.respond(status, error)
		}

		status(HttpStatusCode.NotFound) { call, status ->
			val error = buildErrorResponse(call, status)
			call.respond(status, error)
		}
	}
}
