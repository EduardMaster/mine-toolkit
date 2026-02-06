package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic
import org.bukkit.entity.Player

class MapPos2Command : CommandManager("pos2", "sethigh", "setpos2") {

    override fun playerCommand(player: Player, args: Array<String>) {
        val minigameSchematic = MinigameSchematic.getSchematic(player)
        minigameSchematic.high = player.location.toVector()
        player.sendMessage("§bEduardAPI §aPosicão 2 setada!")
    }

    init {
        description = "Define a posição 2"
    }
}