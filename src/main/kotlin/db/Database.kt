package com.ktor.db

import java.sql.Connection
import java.sql.DriverManager

object Database {

    private const val url = "jdbc:sqlite:database.db"

    init {
        connect().use { conn ->
            conn.createStatement().execute(
                """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    telegram TEXT UNIQUE
                );
                """
            )
        }
    }

    fun connect(): Connection =
        DriverManager.getConnection(url)
}