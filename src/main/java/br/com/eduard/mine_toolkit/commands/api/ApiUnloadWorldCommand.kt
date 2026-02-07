package br.com.eduard.mine_toolkit.commands.api

import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_utils.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiUnloadWorldCommand : CommandManager("unloadworld", "descarregarmundo", "desligarmundo") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        if (Mine.existsWorld(sender, args[0])) {
            Mine.unloadWorld(args[0], true)
            sender.sendMessage("§bEduardAPI §aVoce descarregou o mundo §2" + args[0])
        }
        return true
    }

    init {
        usage = "/api unloadworld <world>"
        description = "Descarrega um mundo carregado no servidor"
    }
}