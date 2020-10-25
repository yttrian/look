package org.yttr

import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import kotlinx.html.ButtonType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.pre
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.luaj.vm2.lib.jse.JsePlatform
import org.yttr.database.Webhook
import org.yttr.database.Webhooks
import org.yttr.partial.respondStandardHTML
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

/**
 * Webhook dispatching and editing
 *
 * Currently does NOT run Lua sandboxed! This is very important to note.
 */
fun Routing.dispatch() {
    suspend fun getWebhook(slug: String) = newSuspendedTransaction {
        Webhook.find {
            Webhooks.slug eq slug
        }.firstOrNull()
    } ?: throw BadRequestException("Invalid slug")

    fun runLuaDangerous(lua: String): String {
        val globals = JsePlatform.standardGlobals()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteArrayOutputStream, true, "utf-8")
        globals.STDOUT = printStream
        globals.STDERR = printStream
        val chunk = globals.load(lua)
        chunk.call()
        return String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8)
    }

    route("/{slug}") {
        get {
            val slug = call.parameters["slug"] ?: error("No slug given")
            val webhook = getWebhook(slug)

            call.respondStandardHTML {
                h1 { +webhook.slug }
                div {
                    id = "editor"
                    pre { +webhook.action }
                }
                div("text-right mt-3 mb-3") {
                    button(classes = "btn btn-danger mr-1", type = ButtonType.button) {
                        id = "delete"
                        +"Delete"
                    }
                    button(classes = "btn btn-secondary mr-1", type = ButtonType.button) {
                        id = "test"
                        +"Test"
                    }
                    button(classes = "btn btn-primary", type = ButtonType.button) {
                        id = "save"
                        +"Save"
                    }
                }
            }
        }

        post {
            val slug = call.parameters["slug"] ?: error("No slug given")
            val webhook = getWebhook(slug)
            call.respond(runLuaDangerous(webhook.action))
        }

        put {
            val slug = call.parameters["slug"] ?: error("No slug given")
            val webhook = getWebhook(slug)
            newSuspendedTransaction {
                webhook.action = call.receiveText()
            }
            call.respond(HttpStatusCode.OK, "Saved $slug")
        }

        delete {
            val slug = call.parameters["slug"] ?: error("No slug given")
            newSuspendedTransaction {
                Webhooks.deleteWhere {
                    Webhooks.slug eq slug
                }
            }
            call.respond("Deleted $slug")
        }
    }
}
