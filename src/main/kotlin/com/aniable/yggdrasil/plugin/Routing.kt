package com.aniable.yggdrasil.plugin

import com.aniable.yggdrasil.error.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	configureStatusPages()

	routing {
		route("/api") {
			get { call.respondText("Hello World!") }
		}
	}
}
