package com.aniable.yggdrasil.error

import io.ktor.http.*

class ResponseStatusException(val status: HttpStatusCode, override val message: String? = null) : RuntimeException()
