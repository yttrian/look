package org.yttr.partial

import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.nav

fun BODY.standardHeader() {
    nav("navbar navbar-dark bg-dark") {
        a("/", classes = "navbar-brand") {
            +"look"
        }
    }
}
