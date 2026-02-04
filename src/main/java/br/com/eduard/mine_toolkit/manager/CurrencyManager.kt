package br.com.eduard.mine_toolkit.manager

import br.com.eduard.eduardapi.EduardAPI
import br.com.eduard.mine_toolkit.server.CurrencySystem
import br.com.eduard.mine_utils.FakePlayer
import br.com.eduard.storage.api.annotations.StorageAttributes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.Locale.getDefault

open class CurrencyManager : CurrencySystem {
    override var name: String = "MoedaCustom"
    override var icon: ItemStack = ItemStack(Material.DIAMOND_BLOCK)
    override var symbol: String = "\$"
    override var displayName: String = "Moeda customizada"
    override var position = 1
    override var inicialAmount = 0
    companion object {
        val currencies = mutableMapOf<String, CurrencySystem>()
        val currenciesByPosition = mutableMapOf<Int, CurrencySystem>()
        fun getNextCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position + 1]
        }
        fun getPreviousCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position - 1]
        }
        fun getCurrencyByIcon(icon: ItemStack): CurrencySystem? {
            return currencies.values.firstOrNull { it.icon == icon }
        }
        fun register(currency: CurrencyManager) {
            var simpleCurrency = currency
            EduardAPI.instance.configs.add("currency." + simpleCurrency.name, simpleCurrency)
            EduardAPI.instance.configs.saveConfig()
            simpleCurrency = EduardAPI.Companion.instance.configs["currency." + simpleCurrency.name, CurrencyManager::class.java]
            EduardAPI.instance.log("§aMoeda registrada: §f" + simpleCurrency.name)
            register(simpleCurrency.name, simpleCurrency)
            currenciesByPosition[simpleCurrency.position] = simpleCurrency
        }
        fun register(currencyName: String, currencyHandler: CurrencySystem) {
            currencies[currencyName.lowercase(getDefault())] = currencyHandler
        }
        fun isRegistred(currencName: String): Boolean {
            return currencies.containsKey(currencName.lowercase(getDefault()))
        }
        fun getCurrency(currencyName: String): CurrencySystem? {
            return currencies[currencyName.lowercase(getDefault())]
        }
    }


    @StorageAttributes(inline = true)
    var currency = mutableMapOf<FakePlayer, Double>()


    override fun get(player: FakePlayer): Double {
        return currency.getOrDefault(player, inicialAmount.toDouble())
    }
    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return get(player) >= amount
    }
    override fun remove(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) - amount)
        return true
    }
    override fun add(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) + amount)
        return true
    }
    override fun set(player: FakePlayer, amount: Double) {
        currency[player] = amount
    }
}