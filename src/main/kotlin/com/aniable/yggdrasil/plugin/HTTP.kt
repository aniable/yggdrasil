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
