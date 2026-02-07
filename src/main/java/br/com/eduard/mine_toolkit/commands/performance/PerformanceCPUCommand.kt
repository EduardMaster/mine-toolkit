package br.com.eduard.mine_toolkit.commands.performance

import com.sun.management.OperatingSystemMXBean
import br.com.eduard.mine_toolkit.kotlin.formatDuration
import br.com.eduard.mine_toolkit.kotlin.percent
import br.com.eduard.mine_toolkit.manager.CommandManager
import org.bukkit.command.CommandSender
import java.lang.management.ManagementFactory

class PerformanceCPUCommand : CommandManager("cpu", "unidadedeprocessamento") {

    init {
        description = "Verifica uso da CPU e detahes dela"
        usage = "/desempenho cpu"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        // val operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        try {
            val operatingSystemMXBean: OperatingSystemMXBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean::class.java
            )
            sender.sendMessage("§eCPU: §a" + operatingSystemMXBean.name)
            sender.sendMessage("§eArquitetura: §a" + operatingSystemMXBean.arch)
            sender.sendMessage("§eProcessadores: §a" + operatingSystemMXBean.availableProcessors)
            sender.sendMessage("§bMedia Em Uso no Computador: §a" + operatingSystemMXBean.systemLoadAverage.percent() + "%")
            sender.sendMessage("§bEm Uso no Computador: §a" + operatingSystemMXBean.systemCpuLoad.percent() + "%")
            sender.sendMessage("§bEm Uso no Processo: §a" + operatingSystemMXBean.processCpuLoad.percent() + "%")
            sender.sendMessage("§dServidor Tempo Ligado: §a" + (operatingSystemMXBean.processCpuTime / 1_000_000).formatDuration())
        } catch (ex: Exception) {
            sender.sendMessage("§cFalha ao puxar dados da Classe Sun sobre o Sistema Operacional.")
            ex.printStackTrace()
        }
    }

}