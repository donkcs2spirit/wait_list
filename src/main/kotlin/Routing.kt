package com.ktor

import com.ktor.db.Database
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/", "static")

        post("/join") {
            val body = call.receiveText()
            val request = kotlinx.serialization.json.Json.decodeFromString<JoinRequest>(body)

            if (request.telegram.isBlank()) {
                call.respond(mapOf("error" to "Empty"))
                return@post
            }

            if (!isValidTelegram(request.telegram)) {
                call.respond(mapOf("error" to "Invalid username"))
                return@post
            }

            try {
                Database.connect().use { conn ->
                    val stmt = conn.prepareStatement(
                        "INSERT INTO users (telegram) VALUES (?)"
                    )
                    stmt.setString(1, request.telegram)
                    stmt.executeUpdate()
                }

                call.respond(mapOf("success" to true))

            } catch (e: Exception) {
                call.respond(mapOf("error" to "Duplicate"))
            }
        }

        get("/count") {

            Database.connect().use { conn ->
                val rs = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) as count FROM users")

                call.respond(mapOf("count" to rs.getInt("count")))
            }
        }
    }
}

fun isValidTelegram(username: String): Boolean {
    val regex = Regex("^@[a-zA-Z0-9_]{4,31}$")

    if (!regex.matches(username)) return false
    if (username.endsWith("_")) return false

    return true
}