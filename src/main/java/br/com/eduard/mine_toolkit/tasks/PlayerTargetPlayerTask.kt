package br.com.eduard.mine_toolkit.tasks

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.mine_toolkit.event.PlayerTargetPlayerEvent
import br.com.eduard.mine_toolkit.kotlin.mineCallEvent
import br.com.eduard.mine_toolkit.manager.TimeManager
import br.com.eduard.mine_utils.Mine

class PlayerTargetPlayerTask : TimeManager(20L) {
    override fun run() {
        for (player in Mine.getPlayers()) {
            try {
                val target = Mine.getTarget(player,
                    Mine.getPlayerAtRange(player.location, 100.0)) ?: continue
                if (target.hasMetadata("NPC"))continue
                MineToolkit.instance.syncTask {
                    PlayerTargetPlayerEvent(
                        target, player).mineCallEvent()
                }
            } catch (ex: Exception) {
                MineToolkit.instance.log("Erro ao executar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }
        }
    }
}
