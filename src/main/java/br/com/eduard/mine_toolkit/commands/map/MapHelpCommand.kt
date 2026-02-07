package br.com.eduard.mine_toolkit.commands.map

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender

class MapHelpCommand : CommandManager("help", "ajuda", "?") {
    override fun command(sender: CommandSender, args: Array<String>) {
        for (sub in parent!!.subCommands.values) {
            if (sender.hasPermission(sub.permission)) {
                sender.sendMessage("§a" + sub.usage + " §8-§7 " + sub.description)
            }
        }
    }
    init {
        description = "Mostra os comandos existentes"
    }
}