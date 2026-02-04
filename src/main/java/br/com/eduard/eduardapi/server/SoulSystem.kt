package br.com.eduard.eduardapi.server

import br.com.eduard.mine_utils.FakePlayer


interface SoulSystem : PluginSystem {
    fun addSouls(player: FakePlayer, amount: Double)
    fun removeSouls(player: FakePlayer, amount: Double)
    fun getSouls(player: FakePlayer): Double
    fun setSouls(player: FakePlayer, amount: Double)
}
