package br.com.eduard.mine_toolkit.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic

import org.bukkit.entity.Player

class MapPos1Command : CommandManager("pos1", "setpos1", "setlow") {

    override fun playerCommand(player: Player, args: Array<String>) {
        val minigameSchematic = MinigameSchematic.newSchematic(player)
        minigameSchematic.low = player.location.toVector()
        player.sendMessage("§bEduardAPI §aPosicão 1 setada!")

    }

    init {
        description = "Define a posição 1"
    }
}