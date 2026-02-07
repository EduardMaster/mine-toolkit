package br.com.eduard.mine_toolkit.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic
import org.bukkit.entity.Player

class MapPasteCommand : CommandManager("paste", "colar") {

    override fun playerCommand(player: Player, args: Array<String>) {
        if (!MinigameSchematic.isEditing(player)) {
            player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
            return
        }
        val map: MinigameSchematic =
            MinigameSchematic.getSchematic(player)
        map.paste(player.location, false)
        player.sendMessage(
            "§bEduardAPI §aMapa colado com sucesso! §2(%blocks)"
                .replace("%blocks", "" + map.count)
        )

    }

    init {
        description = "Cola o Schematic (Mapa) no local que estiver"
    }
}