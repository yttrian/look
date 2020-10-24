package org.yttr

import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.Route

fun Route.staticContent() {
    static {
        static("css") {
            resources("static/css")
        }

        static("js") {
            resources("static/js")
        }
    }
}
