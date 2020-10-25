package org.yttr.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Webhook(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Webhook>(Webhooks)

    val slug by Webhooks.slug
    var action by Webhooks.action
}
