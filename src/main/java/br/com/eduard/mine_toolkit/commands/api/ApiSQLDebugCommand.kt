package br.com.eduard.mine_toolkit.commands.api

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.database.DBManager
import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender

class ApiSQLDebugCommand : CommandManager("sqldebug", "mysqldebug") {
    override fun command(sender: CommandSender, args: Array<String>) {
        if (DBManager.isDebugging) {
            DBManager.isDebugging = false
            MineToolkit.instance.configs["debug.database"] = false
            MineToolkit.instance.configs.saveConfig()
            sender.sendMessage("§cDebug de Database desativado")
        } else {
            DBManager.isDebugging = true
            MineToolkit.instance.configs["debug.database"] = true
            MineToolkit.instance.configs.saveConfig()
            sender.sendMessage("§aDebug de Database ativado")
        }
    }

    init {
        usage = "/api sqldebug"
        description = "Alterna o Debug de Database para Ativado/Desativado"
    }
}