package br.com.eduard.mine_toolkit.game

import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class VisualEffect(var type: Effect = Effect.CLICK1, var data: Int = 0) {

    constructor(type: Effect) : this(type,0)

    fun create(entity: Entity, radius: Int = 10): VisualEffect {
        return create(entity.location, radius)

    }
    fun create(targetPlace: Location, radius: Int = 10): VisualEffect {
        targetPlace.world?.playEffect(targetPlace, type, data, radius)
        return this
    }

    fun create(player: Player): VisualEffect {
        player.playEffect(player.location, type, data)
        return this
    }

}