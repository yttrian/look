package org.yttr.partial

import kotlinx.html.BODY

fun BODY.standardFooter() {
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