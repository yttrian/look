package org.yttr.partial

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.title

suspend fun ApplicationCall.respondStandardHTML(block: BODY.() -> Unit) = respondHtml {
    head {
        title("look")
    }
    body {
        block()
    }
}
