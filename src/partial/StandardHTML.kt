package org.yttr.partial

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.HtmlBlockTag
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.title


suspend fun ApplicationCall.respondStandardHTML(block: HtmlBlockTag.() -> Unit) = respondHtml {
    head {
        title("look")
        css("https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.3/css/bootstrap.min.css") {
            sri = mapOf(
                "sha512" to "oc9+XSs1H243/FRN9Rw62Fn8EtxjEYWHXRvjS43YtueEewbS6ObfXcJNyohjHqVKFPoXXUxwc+q1K7Dee6vv9g=="
            )
        }

    }
    body {
        standardHeader()
        div("container") {
            block()
        }
        standardFooter()
    }
}
