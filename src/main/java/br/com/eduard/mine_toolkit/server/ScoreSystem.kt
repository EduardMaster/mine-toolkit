package br.com.eduard.mine_toolkit.server

import br.com.eduard.mine_toolkit.score.DisplayBoard
import org.bukkit.entity.Player


interface ScoreSystem : PluginSystem{
    fun setScore(player: Player, scoreboard: DisplayBoard)
    fun setScoreDefault(player: Player)
    fun getScore(player: Player): DisplayBoard
    fun hasScore(player: Player): Boolean
}
