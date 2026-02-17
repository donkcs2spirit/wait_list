package com.ktor

import com.ktor.db.Database
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    Database

    configureSerialization()
    configureHTTP()
    configureRouting()
}


@kotlinx.serialization.Serializable
data class JoinRequest(val telegram: String)