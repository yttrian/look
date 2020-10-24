package org.yttr

import io.ktor.http.content.*
import io.ktor.routing.*

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
