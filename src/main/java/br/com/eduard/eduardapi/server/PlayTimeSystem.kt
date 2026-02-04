package br.com.eduard.eduardapi.server

import br.com.eduard.mine_utils.FakePlayer

/**
 * API de Verificar Tempo Jogando (Tempo de Jogo)
 */
interface PlayTimeSystem : PluginSystem{
    fun getPlayTime(player : FakePlayer) : Long
    fun getPlayTimeAverage(player : FakePlayer) : Long
    fun getPlayTimeAverageOfToday(player : FakePlayer) : Long
    fun getStaffPlayTime(player : FakePlayer) : Long
    fun getStaffPlayTimeOfToday(player : FakePlayer) : Long
    fun getStaffPlayTimeOfWeek(player : FakePlayer) : Long
    fun getStaffPlayTimeOfMonth(player : FakePlayer) : Long
}