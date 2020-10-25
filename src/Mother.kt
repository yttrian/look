package org.yttr

import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.input
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.yttr.database.Webhook
import org.yttr.database.Webhooks
import org.yttr.partial.respondStandardHTML

fun Route.mother() {
    get("/") {
        val webhooks = newSuspendedTransaction { Webhook.all().sortedBy { Webhooks.id }.toList() }
        call.respondStandardHTML {
            h1 { +"Webhooks" }
            div("list-group") {
                webhooks.forEach {
                    a("/${it.slug}", classes = "list-group-item list-group-item-action") {
                        +it.slug
                    }
                }
                form(
                    "/new",
                    classes = "list-group-item list-group-item-action",
                    method = FormMethod.post
                ) {
                    div("input-group") {
                        input(InputType.text, classes = "form-control", name = "slug") {
                            placeholder = "New slug"
                        }
                        div("input-group-append") {
                            button(classes = "btn btn-primary", type = ButtonType.submit) {
                                +"Create"
                            }
                        }
                    }
                }
            }
        }
    }

    post("/new") {
        val slug = call.receiveParameters()["slug"]
        if (slug == null || slug == "new") throw BadRequestException("Invalid slug")
        newSuspendedTransaction {
            Webhook.new {
                this.slug = slug
                action = "print 'Hello world!'"
            }
        }
        call.respondRedirect("/$slug")
    }
}
