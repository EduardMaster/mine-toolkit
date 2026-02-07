package br.com.eduard.mine_toolkit.tasks

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.mine_toolkit.manager.TimeManager
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_toolkit.plugin.EduardPlugin
import org.bukkit.Bukkit

class AutoSaveAndBackupTask : TimeManager(60) {
    fun log(msg: String) {
        MineToolkit.instance.log(msg)
    }

    override fun run() {
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin !is EduardPlugin) continue
            val pluginSettings = plugin.settings
            val pluginName = "§e(${plugin.name})"

            try {
                val agora = Extra.getNow()
                val canRunSaveNow = pluginSettings.lastSave + pluginSettings.autoBackupSeconds * 1000 <= agora
                if (pluginSettings.isAutoSave && canRunSaveNow) {
                    log("$pluginName§f Salvando dados do plugin")
                    val startSave = Extra.getNow()
                    plugin.autosave()
                    val endSave = Extra.getNow()
                    log("$pluginName§f Tempo levado para salvar §e" + (endSave - startSave) + "§fms")

                }
            } catch (ex: Exception) {
                log("$pluginName §cFalha ao rodar metodo save()")
                ex.printStackTrace()
            }

            val agora = Extra.getNow()
            val canBackupNow = pluginSettings.lastBackup + pluginSettings.autoBackupSeconds * 1000L < agora
            if (canBackupNow) {
                MineToolkit.instance.asyncTask {
                    try {
                        log("$pluginName§f Gerando Backup")
                        val inicioBackup = Extra.getNow()
                        plugin.backup()
                        val fimBackup = Extra.getNow()
                        log("$pluginName§f Backup gerado com: §e" + (fimBackup - inicioBackup) + "§fms")
                    } catch (ex: Exception) {
                        plugin.creatingBackup = false
                        log("$pluginName §cFalha ao rodar metodo backup()")
                        ex.printStackTrace()
                    }
                }
            }


        }
    }
}
