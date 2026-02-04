package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_utils.Mine
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapLoadCommand : CommandManager("load", "carregar") {
    override fun command(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(sender)
            return
        }
        if (Mine.onlyPlayer(sender)) {
            val player = sender as Player
            val name = args[0].toLowerCase()
            if (MinigameSchematic.exists(name)) {
                MinigameSchematic.loadToCache(player, name)
                player.sendMessage("§bEduardAPI §aMapa carregado com sucesso!")
            } else {
                player.sendMessage("§bEduardAPI §aMapa invalido: §2" + args[1])
            }
        }

    }

    init {
        usage = "/map load <name>"
        description = "Carrega um mundo descarregado"
    }
}