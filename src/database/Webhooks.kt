package org.yttr.database

import org.jetbrains.exposed.dao.id.IntIdTable

object Webhooks : IntIdTable() {
    private const val SLUG_MAX_LENGTH: Int = 50

    val slug = varchar("slug", SLUG_MAX_LENGTH).index()
    val action = text("action")
}
