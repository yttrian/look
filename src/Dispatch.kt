package org.yttr

import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.h1
import kotlinx.html.pre
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.yttr.database.Webhook
import org.yttr.database.Webhooks
import org.yttr.partial.respondStandardHTML

fun Routing.dispatch() {
    suspend fun getWebhook(slug: String) = newSuspendedTransaction {
        Webhook.find {
            Webhooks.slug eq slug
        }.firstOrNull()
    } ?: throw BadRequestException("Invalid slug")

    get("/{slug}") {
        val slug = call.parameters["slug"] ?: error("No slug given")
        val webhook = getWebhook(slug)

        call.respondStandardHTML {
            h1 { +webhook.slug }
            pre { +webhook.action }
        }
    }
}
