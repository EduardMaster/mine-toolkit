package br.com.eduard.eduardapi.commands.api

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiListPluginsCommand : CommandManager("plugins","pluginlist", ) {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage("§bEduardAPI §aLista de Plugins no servidor")
        for (plugin in Bukkit.getPluginManager().plugins) {
            var color = "§c§l"
            if (plugin.isEnabled) {
                color = "§a§l"
            }
            sender.sendMessage(color + plugin.name + " §8- §7" + plugin.description.description)
        }
        return true
    }

    init {
        description = "Mostra todos os plugins do servidor"
    }
}