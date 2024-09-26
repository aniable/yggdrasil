package com.aniable.yggdrasil.plugin

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*

fun Application.configureHTTP() {
	install(CORS) {
		HttpMethod.DefaultMethods.forEach { allowMethod(it) }
		allowHeader(HttpHeaders.Authorization)
		anyHost() // TODO 2024-09-26, 16:07 Define explicit origins
	}

//	install(ForwardedHeaders)
//	install(XForwardedHeaders)

	install(CachingHeaders) {
		options { _, outgoingContent ->
			when (outgoingContent.contentType?.withoutParameters()) {
				ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
				ContentType.Text.JavaScript -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
				else -> null
			}
		}
	}

	install(Compression)

	install(DefaultHeaders) {
		header("X-Engine", "Ktor")
	}
}
