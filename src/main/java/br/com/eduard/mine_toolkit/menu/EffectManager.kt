package br.com.eduard.mine_toolkit.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

import br.com.eduard.mine_toolkit.game.VisualEffect
import br.com.eduard.mine_toolkit.game.SoundEffect
import br.com.eduard.mine_toolkit.hybrid.Hybrid
import br.com.eduard.mine_utils.Mine

open class EffectManager {

    var requirePermission: String? = null
    var messageToSend: String? = null
    var playerCommandsToRun = mutableListOf<String>()
    var consoleCommandsToRun = mutableListOf<String>()
    var itemsToGive = mutableListOf<ItemStack>()
    var potionsToApply = mutableListOf<PotionEffect>()
    var soundToPlay: SoundEffect? = null
    var visualEffectToShow: VisualEffect? = null
    var closeInventory: Boolean = false
    var clearInventory: Boolean = false

    fun playEffects(player: Player) {
        if (requirePermission != null)
            if (!player.hasPermission(requirePermission))
                return
        for (cmd in consoleCommandsToRun) {
            Mine.runCommand(cmd.replace("%player", player.name))
        }
        for (cmd in playerCommandsToRun) {
            Hybrid.instance.console.sendMessage("§eRUNNING CMD: §f/$cmd")
            player.chat(cmd.replace("%player", player.name))
        }
        soundToPlay?.create(player)
        messageToSend?.apply(player::sendMessage)
        if (closeInventory)
            player.closeInventory()
        if (clearInventory) {
            Mine.clearInventory(player)
        }
        visualEffectToShow?.create(player)
        player.inventory.addItem(*itemsToGive.toTypedArray())
        potionsToApply.forEach { it.apply(player) }

    }


}
