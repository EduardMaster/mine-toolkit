package br.com.eduard.mine_toolkit.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic
import org.bukkit.entity.Player
import java.io.File
import java.util.Locale.getDefault

class MapSaveCommand : CommandManager("save", "salvar") {
    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }

        if (!MinigameSchematic.isEditing(player)) {
            player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
            return
        }
        val schema = MinigameSchematic.getSchematic(player)
        schema.name = args[0].lowercase(getDefault())
        schema.register()
        schema.save(File(MinigameSchematic.MAPS_FOLDER, schema.name + ".map"))

        player.sendMessage("§bEduardAPI §aMapa salvado com sucesso!")

    }

    init {
        usage = "/map save <name>"
        description = "Salva o Schematic (Mapa) copiado"
    }
}