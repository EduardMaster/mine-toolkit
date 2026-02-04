package br.com.eduard.eduardapi.tasks

import br.com.eduard.eduardapi.EduardAPI
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
                EduardAPI.instance.syncTask {
                    PlayerTargetPlayerEvent(
                        target, player).mineCallEvent()
                }
            } catch (ex: Exception) {
                EduardAPI.instance.log("Erro ao executar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }
        }
    }
}
