package org.yttr.partial

import kotlinx.html.FlowOrPhrasingOrMetaDataContent
import kotlinx.html.link
import kotlinx.html.script

/**
 * Common web security attributes like SubResource Integrity
 */
class WebSecurity {
    /**
     * Cross-Origin
     */
    var crossorigin = "anonymous"

    /**
     * SubResource Integrity
     */
    var sri: Map<String, String>? = null
}

/**
 * Apply WebSecurity to a tag
 */
fun FlowOrPhrasingOrMetaDataContent.apply(ws: WebSecurity.() -> Unit) = WebSecurity().apply {
    ws()
    sri?.let { attributes["integrity"] = it.map { (type, hash) -> "$type-$hash" }.joinToString() }
    attributes["crossorigin"] = crossorigin
}

/**
 * Simplified link tag for CSS with WebSecurity
 */
fun FlowOrPhrasingOrMetaDataContent.css(href: String, ws: WebSecurity.() -> Unit = {}) =
    link(href, "stylesheet") {
        apply(ws)
    }

/**
 * Simplified script tag for JS with WebSecurity
 */
fun FlowOrPhrasingOrMetaDataContent.javascript(src: String, ws: WebSecurity.() -> Unit = {}) =
    script("text/javascript", src) {
        apply(ws)
    }
