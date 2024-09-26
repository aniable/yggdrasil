package com.aniable.yggdrasil

import com.aniable.yggdrasil.plugin.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
	io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
	configureSecurity()
	configureHTTP()
	configureMonitoring()
	configureSerialization()
	configureDatabases()
	configureRouting()
}
