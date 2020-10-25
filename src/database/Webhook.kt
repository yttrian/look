package org.yttr.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Webhook DAO
 */
class Webhook(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Webhook>(Webhooks)

    /**
     * URL slug
     */
    var slug by Webhooks.slug

    /**
     * Lua code to run
     */
    var action by Webhooks.action
}
