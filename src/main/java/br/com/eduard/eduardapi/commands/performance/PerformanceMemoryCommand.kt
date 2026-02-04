package br.com.eduard.eduardapi.commands.performance

import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceMemoryCommand : CommandManager("memory", "ram", "men") {

    init {
        description = "Verifica o uso de Memoria RAM"
        usage = "/desempenho ram"
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceGarbageColectorCommand())
        register(_root_ide_package_.br.com.eduard.eduardapi.command.performance.PerformanceCPUCommand())
    }

    override fun command(sender: CommandSender, args: Array<String>) {

        val div = 1000 * 1000
        val memoriaDisponivel = (Runtime.getRuntime().freeMemory() / div)
        val totalMemoria = (Runtime.getRuntime().totalMemory() / div)
        val maximoMemoria = Runtime.getRuntime().maxMemory() / div
        val memoriaUsada = totalMemoria - memoriaDisponivel
        sender.sendMessage("§bVerificador de uso de Memoria")
        sender.sendMessage("§eMemoria Disponivel: §a${memoriaDisponivel}MB")
        sender.sendMessage("§eMemoria Total: §a${totalMemoria}MB")
        sender.sendMessage("§eMemoria Maxima: §c${maximoMemoria}MB")
        sender.sendMessage("§eMemoria Usada: §c${memoriaUsada}MB")

    }

}