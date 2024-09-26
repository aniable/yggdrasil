package com.aniable.yggdrasil.plugin

import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureDatabases() {
	val database = Database.connect(
		url = environment.config.property("db.url").getString(),
		driver = environment.config.property("db.driver").getString(),
		user = environment.config.property("db.user").getString(),
		password = environment.config.property("db.password").getString(),
	)

	suspend fun <T> query(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
		block()
	}
}
