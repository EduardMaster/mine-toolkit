package br.com.eduard.mine_toolkit.listeners

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.mine_toolkit.core.PlayerSkin
import br.com.eduard.mine_toolkit.manager.EventsManager

import br.com.eduard.mine_toolkit.event.BlockMineEvent
import br.com.eduard.mine_toolkit.kotlin.mineCallEvent
import br.com.eduard.mine_toolkit.plugin.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent

/**
 * Pequenas manipulações de Eventos criados que qualquer servidor precise
 *
 * @since 2.3
 * @version 1.0
 *
 * @author Eduard
 */
class MineToolkitListener : EventsManager() {

    var minerationEventEnabled = MineToolkit.instance.getBoolean("features.block-mine-event")

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBreakCallMineEvent(event: BlockBreakEvent) {
        val block = event.block
        val type = block.type
        if (type == Material.AIR ||
            type == Material.ICE ||
            type == Material.PACKED_ICE ||
            type == Material.CHEST ||
            type == Material.TRAPPED_CHEST ||
            type == Material.LEGACY_SKULL_ITEM ||
            type == Material.ITEM_FRAME ||
            type == Material.ENDER_CHEST ||
            type == Material.BEDROCK ||
            type == Material.LEGACY_SIGN_POST ||
            type == Material.LEGACY_MOB_SPAWNER ||
            type == Material.LEGACY_WALL_SIGN
        ) {
            return
        }
        if (!minerationEventEnabled) return
        if (event.player.gameMode == GameMode.CREATIVE) return
        event.isCancelled = true
        event.expToDrop = 0
        val newEvent = BlockMineEvent(event, event.player, block, mutableMapOf(), true, event.expToDrop)
        newEvent.mineCallEvent()
        newEvent.defaultEventActions()

    }

    @EventHandler
    fun onJoinShowPlugins(e: PlayerJoinEvent) {
        val player = e.player
        if (MineToolkit.instance.getBoolean("features.skins")) {
            PlayerSkin.change(player, player.name)
        }
        if (!MineToolkit.instance.getBoolean("features.show-plugins")) return
        if (!player.hasPermission("eduardapi.plugins")) return
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin !is EduardPlugin) continue
            if (plugin.isEnabled()) {
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().version + "§a esta ativado.")
            } else {
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().version + "§c esta desativado.")
            }
        }
        player.sendMessage("§aCaso deseje comprar mais plugins entre em contato ou no site §bwww.eduard.com.br")

    }

    @EventHandler
    fun onDeathAutoRespawn(e: PlayerDeathEvent) {
        val player = e.entity
        if (MineToolkit.instance.getBoolean("features.auto-respawn")) {
            MineToolkit.instance.syncDelay(1) {
                if (player.isDead) {
                    player.fireTicks = 0
                    try {
                        player.spigot().respawn()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }

}
