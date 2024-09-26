package com.aniable.yggdrasil.error

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
	@SerialName("status_code") val statusCode: Int,
	@SerialName("status_reason") val statusReason: String,
	val message: String? = null,
	val path: String? = null,
	val timestamp: Instant? = null,
)
