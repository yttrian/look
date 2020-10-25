package org.yttr

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.BadRequestException
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.cio.EngineMain
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import org.yttr.database.Webhooks
import java.net.URI

/**
 * Entry point of look
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)

/**
 * Database connection
 */
val db by lazy {
    val dbURI = URI(System.getenv("DATABASE_URL"))
    val dbSSLMode = System.getenv("DATABASE_SSL_MODE") ?: "require"
    val userInfo = dbURI.userInfo.split(":")
    val username = userInfo[0]
    val password = userInfo[1]
    val connectionUrl = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=$dbSSLMode"

    Database.connect(connectionUrl, driver = "org.postgresql.Driver", user = username, password = password)
}

/**
 * Main module
 */
fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ContentNegotiation)
    install(StatusPages) {
        exception<BadRequestException> { cause ->
            HttpStatusCode.BadRequest.let {
                call.respond(it, "${it.value}: ${cause.localizedMessage}")
            }
        }
    }

    transaction(db) {
        SchemaUtils.create(Webhooks)
    }

    routing {
        mother()
        staticContent()
        dispatch()
    }
}
