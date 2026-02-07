package br.com.eduard.mine_toolkit.commands.api

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiHelpCommand : CommandManager("help", "ajuda", "?") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage("")
        for (subCMD in parent!!.subCommands.values) {
            if (sender.hasPermission(subCMD.permission)) {
                sender.sendMessage("§b" + subCMD.usage + " §8-§3 " + subCMD.description)
            }
        }
        return true
    }

    init {
        description = "Mostra uma lista de comandos"
    }
}