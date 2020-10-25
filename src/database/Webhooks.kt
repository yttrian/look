package org.yttr.database

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Webhook table
 */
object Webhooks : IntIdTable() {
    private const val SLUG_MAX_LENGTH: Int = 50

    /**
     * URL slug
     */
    val slug = varchar("slug", SLUG_MAX_LENGTH).uniqueIndex()

    /**
     * Lua code to run
     */
    val action = text("action")
}
