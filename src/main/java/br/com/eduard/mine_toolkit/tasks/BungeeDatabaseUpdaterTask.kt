package br.com.eduard.mine_toolkit.tasks

import br.com.eduard.mine_toolkit.MineToolkitBungee
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_toolkit.plugin.EduardBungeePlugin
import net.md_5.bungee.api.ProxyServer

class BungeeDatabaseUpdaterTask : Runnable {
    fun log(msg: String) {
        MineToolkitBungee.instance.log(msg)
    }

    override fun run() {
        for (plugin in ProxyServer.getInstance().pluginManager.plugins) {
            if (plugin !is EduardBungeePlugin) continue
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
        }
    }
}