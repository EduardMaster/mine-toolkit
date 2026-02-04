package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.java_utils.Extra
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.Material
import org.bukkit.entity.Player

class MapSetCommand : CommandManager("set", "setblock") {
    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()){
           sendUsage(player)
            return
        }
        val typeId = Extra.toInt(args[0]);
        var dataId = 0
        if (args.size>=2){
            dataId = Extra.toInt(args[1]);
        }
        val mat = Material.getMaterial(typeId)
        val worldUsed = player.location.world
        val minigameSchematic =  MinigameSchematic.getSchematic(player)
        minigameSchematic.prepare(player.location, minigameSchematic.low.toLocation(worldUsed), minigameSchematic.high.toLocation(worldUsed))
        minigameSchematic.setType(typeId.toShort(),dataId.toByte())
        minigameSchematic.paste(player.location,true)
        player.sendMessage("§bEduardAPI §aSetando blocos para §e$mat")
    }

    init {
        usage = "/map set <blockID>"
        description = "Altera o tipo dos blocos da Posição 1 ate a Posção 2"
    }
}