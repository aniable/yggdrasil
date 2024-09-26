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
