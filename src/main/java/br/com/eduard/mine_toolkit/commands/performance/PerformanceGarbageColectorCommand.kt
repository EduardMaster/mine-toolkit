package br.com.eduard.mine_toolkit.commands.performance

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceGarbageColectorCommand : CommandManager("garbagecolector", "gc") {

    init {
        description = "Indica para JVM forçar "
        usage = "/desempenho gc"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        Runtime.getRuntime().gc()
        sender.sendMessage("§aAtivando GC de Memoria no Java.")

    }

}