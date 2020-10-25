package org.yttr.partial

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.HtmlBlockTag
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.main
import kotlinx.html.title

/**
 * Like respondHtml but includes standard header and footer
 */
suspend fun ApplicationCall.respondStandardHTML(block: HtmlBlockTag.() -> Unit) = respondHtml {
    head {
        title("look")
        css("https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.3/css/bootstrap.min.css") {
            sri = mapOf(
                "sha512" to "oc9+XSs1H243/FRN9Rw62Fn8EtxjEYWHXRvjS43YtueEewbS6ObfXcJNyohjHqVKFPoXXUxwc+q1K7Dee6vv9g=="
            )
        }
        css("https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.21.2/min/vs/editor/editor.main.min.css") {
            sri = mapOf(
                "sha512" to "9uX8QlyL0SosYXO3oNqyiXdnmhtWk22wutqEzGR53Bezc+yqYVvFukBAOW97fPx/3Dxdul77zW27GwHRzdYfMg=="
            )
        }
        css("css/site.css")
    }
    body {
        standardHeader()
        main("container") {
            block()
        }
        standardFooter()
    }
}
