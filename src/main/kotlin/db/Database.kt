package com.ktor.db

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private val jdbcUrl: String by lazy {
        System.getenv("DATABASE_URL")
            ?: error("DATABASE_URL not set")
    }

    init {
        Class.forName("org.postgresql.Driver")

        connect().use { conn ->
            conn.createStatement().executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    telegram TEXT UNIQUE NOT NULL
                );
                """.trimIndent()
            )
        }
    }

    fun connect(): Connection {
        return DriverManager.getConnection(jdbcUrl)
    }
}