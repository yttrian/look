package org.yttr.partial

import io.ktor.application.ApplicationCall
import io.ktor.html.respondHtml
import kotlinx.html.FlowOrPhrasingOrMetaDataContent
import kotlinx.html.HtmlBlockTag
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.title

private class WebSecurity {
    var crossorigin = "anonymous"
    var sri: Map<String, String>? = null
}

private fun FlowOrPhrasingOrMetaDataContent.apply(ws: WebSecurity.() -> Unit) = WebSecurity().apply {
    ws()
    sri?.let { attributes["integrity"] = it.map { (type, hash) -> "$type-$hash" }.joinToString() }
    attributes["crossorigin"] = crossorigin
}

private fun FlowOrPhrasingOrMetaDataContent.css(href: String, ws: WebSecurity.() -> Unit = {}) =
    link(href, "stylesheet") {
        apply(ws)
    }

private fun FlowOrPhrasingOrMetaDataContent.javascript(src: String, ws: WebSecurity.() -> Unit = {}) =
    script("text/javascript", src) {
        apply(ws)
    }

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
        div("container") {
            block()
        }
        javascript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js") {
            sri = mapOf(
                "sha512" to "bLT0Qm9VnAYZDflyKcBaQ2gg0hSYNQrJ8RilYldYQ1FxQYoCLtUjuuRuZo+fjqhx/qtq/1itJ0C2ejDxltZVFg=="
            )
        }
        javascript("https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.3/js/bootstrap.bundle.min.js") {
            sri = mapOf(
                "sha512" to "iceXjjbmB2rwoX93Ka6HAHP+B76IY1z0o3h+N1PeDtRSsyeetU3/0QKJqGyPJcX63zysNehggFwMC/bi7dvMig=="
            )
        }
    }
}
