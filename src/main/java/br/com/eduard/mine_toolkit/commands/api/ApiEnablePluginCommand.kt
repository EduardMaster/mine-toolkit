package br.com.eduard.mine_toolkit.commands.api

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_utils.Mine
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiEnablePluginCommand : CommandManager("enableplugin", "habilitarplugin") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        val pluginName = args[0]
        if (Mine.existsPlugin(sender, pluginName)) {
            val plugin = Mine.getPlugin(pluginName)
            sender.sendMessage("§bEduardAPI §aPlugin §f$pluginName §afoi ativado")
            Bukkit.getPluginManager().enablePlugin(plugin)
        }

        return true
    }

    init {
        usage = "/api enableplugin <plugin>"
        description = "Ativa um plugin desativado no servidor"
    }
}