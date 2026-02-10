package br.com.eduard.mine_toolkit.manager

import java.util.UUID
import org.bukkit.entity.Player

/**
 * A comprehensive manager for handling player-specific cooldowns in a Minecraft environment.
 *
 * This class automates timing, messaging, and task execution associated with cooldown periods.
 * It uses server ticks for precision and supports customizable notification messages.
 *
 * @property duration The cooldown duration in server ticks (default is 20 ticks / 1 second).
 * @author Eduard
 * @since 1.0
 */
open class CooldownManager(var duration: Long = 20) {

    /**
     * Message sent when a player attempts to perform an action while still on cooldown.
     * Use `%time` as a placeholder for the remaining seconds.
     */
    var messageOnCooldown: String = ""

    /**
     * Message sent when the cooldown period expires.
     */
    var messageCooldownOver: String = ""

    /**
     * Message sent when the player is first placed on cooldown.
     */
    var messageCooldownStart: String = ""

    init {
        messageOnCooldown = "§cAinda faltam §7%time segundos §cpara desabilitar o sistema"
    }

    /**
     * Clears all notification messages, making the cooldown system silent.
     */
    fun noMessages() {
        messageOnCooldown = ""
        messageCooldownOver = ""
        messageCooldownStart = ""
    }

    /**
     * Active cooldown tasks mapped by the Player's unique ID.
     */
    @Transient
    val cooldowns = mutableMapOf<UUID, TimeManager>()

    /**
     * Attempts to trigger the cooldown for a player.
     *
     * @param player The player to check.
     * @return `true` if the player was NOT on cooldown and is now starting it;
     * `false` if they are already on cooldown (sends [messageOnCooldown]).
     */
    fun cooldown(player: Player): Boolean {
        if (onCooldown(player)) {
            sendOnCooldown(player)
            return false
        }
        setOnCooldown(player)
        sendStartCooldown(player)
        return true
    }

    /**
     * Forces the removal of a player's cooldown and cancels the associated [TimeManager] task.
     * @param player The player to release from cooldown.
     */
    fun stopCooldown(player: Player) {
        val timeManager = cooldowns[player.uniqueId]
        timeManager?.stopTask()
        cooldowns.remove(player.uniqueId)
    }

    /**
     * Checks if the player is currently restricted by an active cooldown.
     * @param player The player to check.
     * @return `true` if the cooldown is active, `false` otherwise.
     */
    fun onCooldown(player: Player): Boolean {
        return getResult(player) > 0
    }

    /**
     * Internal task that handles the asynchronous delay and triggers
     * the [sendOverCooldown] notification upon completion.
     */
    inner class CooldownOverTask(val player: Player) : TimeManager(duration) {
        override fun run() {
            sendOverCooldown(player)
        }
        init {
            asyncDelay()
        }
    }

    /**
     * Manually places a player on cooldown. If an active cooldown exists,
     * it is stopped and replaced by a new instance.
     * * @param player The target player.
     * @return This [CooldownManager] instance for fluent API chaining.
     */
    fun setOnCooldown(player: Player): CooldownManager {
        if (onCooldown(player)) {
            stopCooldown(player)
        }
        cooldowns[player.uniqueId] = CooldownOverTask(player)
        return this
    }

    /**
     * Sends the [messageCooldownOver] to the player if it is defined.
     */
    fun sendOverCooldown(player: Player) {
        if (messageCooldownOver.isNotEmpty())
            player.sendMessage(messageCooldownOver)
    }

    /**
     * Sends the [messageOnCooldown] to the player, replacing the `%time` placeholder
     * with the remaining seconds.
     */
    fun sendOnCooldown(player: Player) {
        if (messageOnCooldown.isNotEmpty())
            player.sendMessage(
                messageOnCooldown
                    .replace("%time", "" + getCooldown(player))
            )
    }

    /**
     * Sends the [messageCooldownStart] to the player if it is defined.
     */
    fun sendStartCooldown(player: Player) {
        if (messageCooldownStart.isNotEmpty())
            player.sendMessage(messageCooldownStart)
    }

    /**
     * Calculates the remaining time for the player's cooldown in server ticks.
     *
     * @param player The player to check.
     * @return The number of ticks remaining, or 0 if the cooldown has expired or doesn't exist.
     */
    fun getResult(player: Player): Long {
        if (cooldowns.containsKey(player.uniqueId)) {
            val now = System.currentTimeMillis()
            val timeManager = cooldowns[player.uniqueId]!!
            val before = timeManager.taskStart
            val cooldownDuration = timeManager.taskDuration * 50
            val endOfCooldown = before + cooldownDuration
            val durationLeft = endOfCooldown - now
            return if (durationLeft <= 0) {
                0
            } else durationLeft / 50
        }
        return 0
    }

    /**
     * Converts the remaining cooldown time into seconds for display purposes.
     *
     * @param player The player to check.
     * @return Remaining seconds (rounded up to the next integer).
     */
    fun getCooldown(player: Player): Int {
        return (getResult(player) / 20).toInt() + 1
    }
}