package br.com.eduard.mine_toolkit.commands

import br.com.eduard.mine_toolkit.core.PlayerSkin
import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.entity.Player

class SetSkinCommand : CommandManager("setskin","setplayerskin") {
    init{
        description= "Defina uma Skin para si mesmo"
        usage= "/<command> <playerName>"
    }
    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }
        val playerName = args[0]
        player.sendMessage("§aSua skin foi alterada para $playerName")
        PlayerSkin.change(player, playerName)
    }

}