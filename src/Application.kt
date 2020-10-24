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
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.EngineMain
import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.ul
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import org.yttr.database.Webhook
import org.yttr.database.Webhooks
import org.yttr.partial.respondStandardHTML
import java.net.URI

fun main(args: Array<String>): Unit = EngineMain.main(args)

val db by lazy {
    val dbURI = URI(System.getenv("DATABASE_URL"))
    val dbSSLMode = System.getenv("DATABASE_SSL_MODE") ?: "require"
    val userInfo = dbURI.userInfo.split(":")
    val username = userInfo[0]
    val password = userInfo[1]
    val connectionUrl = "jdbc:postgresql://${dbURI.host}:${dbURI.port}${dbURI.path}?sslmode=$dbSSLMode"

    Database.connect(connectionUrl, driver = "org.postgresql.Driver", user = username, password = password)
}

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
        get("/") {
            val webhooks = newSuspendedTransaction { Webhook.all().toList() }
            call.respondStandardHTML {
                h1 { +"Webhooks" }
                ul {
                    webhooks.forEach {
                        li { a("/${it.slug}") { +it.slug } }
                    }
                }
            }
        }

        staticContent()
        dispatch()
    }
}
