package br.com.eduard.eduardapi.commands.performance

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceCommand : CommandManager("performance", "desempenho","checklag") {

    init {
        description = "Verifica Desempenho do Servidor e derivados"
        usage = "/desempenho "
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceGarbageColectorCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceCPUCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceMemoryCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceEntitiesCommand())
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        for (subCmd in subCommands.values){
            sender.sendMessage("§b${subCmd.usage} §8- §7${subCmd.description}")
        }
    }
}