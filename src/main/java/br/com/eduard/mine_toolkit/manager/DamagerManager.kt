package br.com.eduard.mine_toolkit.manager

import br.com.eduard.mine_toolkit.MineToolkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * A specialized manager designed to track and store information about the most recent
 * damage interactions between entities.
 *
 * This system is essential for handling combat tags, kill attribution, and identifying
 * the true source of damage, including resolving shooters from projectiles.
 * * @author Eduard
 * @since 1.7
 */
object DamagerManager : EventsManager() {

    /**
     * Internal map storing the last [Entity] that damaged a specific target.
     */
    private val lastPvP: MutableMap<Entity, Entity> = mutableMapOf()

    /**
     * Internal map storing the timestamp (in milliseconds) of the last hit taken by an entity.
     */
    private val lastHitTaken = mutableMapOf<Entity, Long>()

    init {
        register(MineToolkit.instance)
    }

    /**
     * Retrieves the exact system time when the specified entity was last damaged.
     * * @param entity The target entity to check.
     * @return The timestamp in milliseconds, or 0 if no record exists.
     */
    fun getLastDamageMoment(entity: Entity): Long {
        return lastHitTaken.getOrElse(entity) { 0 }
    }

    /**
     * Identifies the last damager of an entity.
     * * This method automatically resolves [Projectile] sources (e.g., if an arrow hits a player,
     * it returns the Archer instead of the Arrow entity).
     * * @param entity The target entity.
     * @return The attacking [Entity], or the shooter of the projectile, or null if not found.
     */
    fun getLastDamager(entity: Entity): Entity? {
        val damager = lastPvP[entity] ?: return null
        if (damager is Projectile) {
            if (damager.shooter != null && damager.shooter is Entity) {
                return damager.shooter as Entity
            }
        }
        return damager
    }

    /**
     * Updates the combat tracking records whenever an entity takes damage from another entity.
     * Recorded at [EventPriority.HIGHEST] and ignores cancelled events to ensure data accuracy.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onDamage(e: EntityDamageByEntityEvent) {
        lastPvP[e.entity] = e.damager
        lastHitTaken[e.entity] = System.currentTimeMillis()
    }

    /**
     * Cleans up tracking data when a player leaves the server to prevent memory leaks.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onQuit(e: PlayerQuitEvent) {
        lastPvP.remove(e.player)
        lastHitTaken.remove(e.player)
    }
}