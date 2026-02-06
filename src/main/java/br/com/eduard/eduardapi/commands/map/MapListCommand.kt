package br.com.eduard.eduardapi.commands.map

import br.com.eduard.mine_toolkit.kotlin.text
import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic
import org.bukkit.command.CommandSender
import java.nio.file.Files

class MapListCommand : CommandManager("list", "lsitar", "status") {
    override fun command(sender: CommandSender, args: Array<String>) {
        sender.sendMessage("§3Mapas carregados:")
        for (map in MinigameSchematic.getSchematics()){
            sender.sendMessage("§bNome: "+map.name+" High: "+map.height.text
                    + " Length: "+map.length.text + " Width: "+ map.width.text)
        }
        sender.sendMessage("§6Mapas no HD:")
        for (subFile in MinigameSchematic.MAPS_FOLDER.listFiles()!!){
            val spaceUsed = Files.size(subFile.toPath())
            sender.sendMessage("§e"+subFile.name+" §f"+((spaceUsed).text)+" bytes")
        }
    }

    init {
        description = "Mostra os comandos existentes"
    }
}