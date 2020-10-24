package org.yttr.partial

import kotlinx.html.FlowOrPhrasingOrMetaDataContent
import kotlinx.html.link
import kotlinx.html.script

class WebSecurity {
    var crossorigin = "anonymous"
    var sri: Map<String, String>? = null
}

fun FlowOrPhrasingOrMetaDataContent.apply(ws: WebSecurity.() -> Unit) = WebSecurity().apply {
    ws()
    sri?.let { attributes["integrity"] = it.map { (type, hash) -> "$type-$hash" }.joinToString() }
    attributes["crossorigin"] = crossorigin
}

fun FlowOrPhrasingOrMetaDataContent.css(href: String, ws: WebSecurity.() -> Unit = {}) =
    link(href, "stylesheet") {
        apply(ws)
    }

fun FlowOrPhrasingOrMetaDataContent.javascript(src: String, ws: WebSecurity.() -> Unit = {}) =
    script("text/javascript", src) {
        apply(ws)
    }
