package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.server.minigame.MinigameSchematic
import org.bukkit.entity.Player

class MapCopyCommand : CommandManager("copy", "copiar") {

    override fun playerCommand(player: Player, args: Array<String>) {
        val minigameSchematic  =
            MinigameSchematic.getSchematic(player)
        minigameSchematic.copy(player.location)
        player.sendMessage("§bEduardAPI §aMapa copiado!")
    }


    init {
        description = "Copia os blocos da posição 1 a posção 2"
    }
}