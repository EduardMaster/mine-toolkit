package br.com.eduard.eduardapi.server

import br.com.eduard.mine_utils.FakePlayer

interface CashSystem : PluginSystem {

    fun addCash(player: FakePlayer, amount: Double)

    fun removeCash(player: FakePlayer, amount: Double)

    fun getCash(player: FakePlayer): Double

    fun setCash(player: FakePlayer, amount: Double)

    fun hasCash(player: FakePlayer, amount: Double): Boolean {
        return getCash(player) >= amount
    }

}
