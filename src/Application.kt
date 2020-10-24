package org.yttr

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.path
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.EngineMain
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import org.yttr.database.Webhooks
import java.net.URI

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation)

    Database.apply {
        val dbURI = URI(System.getenv("DATABASE_URL"))
        val dbSSLMode = System.getenv("DATABASE_SSL_MODE") ?: "require"
        val userInfo = dbURI.userInfo.split(":")
        val username = userInfo[0]
        val password = userInfo[1]
        val connectionUrl = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=$dbSSLMode"

        connect(connectionUrl, driver = "org.postgresql.Driver", user = username, password = password)
    }

    transaction {
        SchemaUtils.create(Webhooks)
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        staticContent()
    }
}
