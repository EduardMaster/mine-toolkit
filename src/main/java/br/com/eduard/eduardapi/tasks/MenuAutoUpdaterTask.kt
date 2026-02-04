package br.com.eduard.eduardapi.tasks

import br.com.eduard.eduardapi.EduardAPI
import br.com.eduard.mine_toolkit.manager.TimeManager
import br.com.eduard.mine_toolkit.menu.getMenu
import br.com.eduard.mine_utils.Mine
import java.lang.Exception

class MenuAutoUpdaterTask : TimeManager(EduardAPI.instance.configs
    .getLong("menu-updater.ticks")) {

    override fun run() {
        for (player in Mine.getPlayers()) {
            val menu = player.getMenu() ?: continue
            try {
                val pagina = menu.getPageOpen(player)
                val inventory = player.openInventory.topInventory
                menu.update(inventory,player,pagina , false)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}