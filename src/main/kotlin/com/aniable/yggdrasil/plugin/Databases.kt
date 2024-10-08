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

import com.aniable.yggdrasil.feature.user.Users
import com.password4j.Password
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
	val database = Database.connect(
		url = environment.config.property("db.url").getString(),
		driver = environment.config.property("db.driver").getString(),
		user = environment.config.property("db.user").getString(),
		password = environment.config.property("db.password").getString(),
	)

	transaction(database) {
		SchemaUtils.create(Users)

		Users.insertIgnore {
			it[email] = "user@example.com"
			it[username] = "user"
			it[password] = Password.hash("changeme").addRandomSalt().withArgon2().result
		}
	}
}

suspend fun <T> query(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
	block()
}
