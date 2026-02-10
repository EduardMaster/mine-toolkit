package br.com.eduard.mine_toolkit.manager

import br.com.eduard.mine_toolkit.MineToolkit
import br.com.eduard.mine_toolkit.server.CurrencySystem
import br.com.eduard.mine_utils.FakePlayer
import br.com.eduard.storage.annotations.StorageAttributes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.Locale.getDefault

/**
 * A flexible system for managing custom currencies within the server.
 *
 * This class implements [CurrencySystem] and provides static utilities to register,
 * retrieve, and navigate through different currencies based on their priority (position).
 *
 * * @author Eduard
 * @since 1.7
 */
open class CurrencyManager : CurrencySystem {

    override var name: String = "MoedaCustom"
    override var icon: ItemStack = ItemStack(Material.DIAMOND_BLOCK)
    override var symbol: String = "\$"
    override var displayName: String = "Moeda customizada"
    override var position = 1
    override var inicialAmount = 0

    companion object {
        /**
         * Global registry of all currencies indexed by their lowercase name.
         */
        val currencies = mutableMapOf<String, CurrencySystem>()

        /**
         * Mapping of currencies by their hierarchy position for progression systems.
         */
        val currenciesByPosition = mutableMapOf<Int, CurrencySystem>()

        /**
         * Retrieves the next currency in the hierarchy based on position.
         */
        fun getNextCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position + 1]
        }

        /**
         * Retrieves the previous currency in the hierarchy based on position.
         */
        fun getPreviousCurrency(currency: CurrencySystem): CurrencySystem? {
            return currenciesByPosition[currency.position - 1]
        }

        /**
         * Finds a currency that matches the provided [ItemStack] icon.
         */
        fun getCurrencyByIcon(icon: ItemStack): CurrencySystem? {
            return currencies.values.firstOrNull { it.icon == icon }
        }

        /**
         * Registers a currency manager into the toolkit's configuration system
         * and initializes it in the global memory maps.
         */
        fun register(currency: CurrencyManager) {
            var simpleCurrency = currency
            MineToolkit.instance.configs.add("currency." + simpleCurrency.name, simpleCurrency)
            MineToolkit.instance.configs.saveConfig()
            simpleCurrency = MineToolkit.Companion.instance.configs["currency." + simpleCurrency.name, CurrencyManager::class.java]
            MineToolkit.instance.log("§aMoeda registrada: §f" + simpleCurrency.name)
            register(simpleCurrency.name, simpleCurrency)
            currenciesByPosition[simpleCurrency.position] = simpleCurrency
        }

        /**
         * Registers a currency handler directly into the memory map.
         */
        fun register(currencyName: String, currencyHandler: CurrencySystem) {
            currencies[currencyName.lowercase(getDefault())] = currencyHandler
        }

        /**
         * Checks if a currency is already registered.
         */
        fun isRegistred(currencName: String): Boolean {
            return currencies.containsKey(currencName.lowercase(getDefault()))
        }

        /**
         * Retrieves a registered currency by its name.
         */
        fun getCurrency(currencyName: String): CurrencySystem? {
            return currencies[currencyName.lowercase(getDefault())]
        }
    }

    /**
     * Internal storage for player balances.
     * Uses [FakePlayer] to support both online players and offline/virtual entities.
     */
    @StorageAttributes(inline = true)
    var currency = mutableMapOf<FakePlayer, Double>()

    /**
     * Gets the current balance of a player.
     * Returns [inicialAmount] if the player has no record.
     */
    override fun get(player: FakePlayer): Double {
        return currency.getOrDefault(player, inicialAmount.toDouble())
    }

    /**
     * Checks if a player has at least the specified [amount].
     */
    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return get(player) >= amount
    }

    /**
     * Subtracts the specified [amount] from the player's balance.
     */
    override fun remove(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) - amount)
        return true
    }

    /**
     * Adds the specified [amount] to the player's balance.
     */
    override fun add(player: FakePlayer, amount: Double): Boolean {
        set(player, get(player) + amount)
        return true
    }

    /**
     * Sets the player's balance to a specific [amount].
     */
    override fun set(player: FakePlayer, amount: Double) {
        currency[player] = amount
    }
}