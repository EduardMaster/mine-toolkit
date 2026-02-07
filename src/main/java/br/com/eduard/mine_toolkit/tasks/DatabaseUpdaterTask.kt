package br.com.eduard.mine_toolkit.tasks

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_toolkit.plugin.EduardPlugin
import org.bukkit.Bukkit

class DatabaseUpdaterTask : Thread("EduardAPI Databases Updater") {

    fun log(msg: String) {
        MineToolkit.instance.log(msg)
    }

    override fun run() {
        while (true) {
            try {
                sleep(1000L)
            } catch (ex: InterruptedException) {
                break
            }
            for (plugin in Bukkit.getPluginManager().plugins) {
                if (plugin !is EduardPlugin) continue
                try {
                    if (plugin.dbManager.hasConnection()) {
                        run {
                            val agora = Extra.getNow()
                            val amountChanges = plugin.sqlManager.runChanges()
                            val tempoDepois = Extra.getNow()
                            val tempoPercorrido = tempoDepois - agora
                            if (amountChanges > 0)
                                plugin.log("Database Update: §e$amountChanges §fChanges in §c${tempoPercorrido}ms")
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
}