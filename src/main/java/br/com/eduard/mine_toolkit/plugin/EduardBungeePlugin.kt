package br.com.eduard.mine_toolkit.plugin

import br.com.eduard.database.DBManager
import br.com.eduard.database.DatabaseManager
import br.com.eduard.database.HybridTypes
import br.com.eduard.database.SQLManager
import br.com.eduard.mine_toolkit.config.Config
import br.com.eduard.mine_toolkit.kotlin.resolvePut
import br.com.eduard.mine_toolkit.kotlin.resolveTake
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

/**
 * Todos plugins para BungeeCord e Waterfall feitos pelo Eduard extendem esta classe<br></br>
 */
class EduardBungeePlugin : Plugin(), IPluginInstance<Plugin> {


    val started: Boolean = false
    val isFree: Boolean = false

    var prefix: String? = null
        get() {
            if (field == null) {
                field = "[" + this.pluginName + "] "
            }
            return field
        }
        private set
    lateinit var config: Config
    lateinit var messages: Config
    lateinit var storage: Config
    lateinit var settings: PluginSettings
    lateinit var dbManager: DBManager
    lateinit var sqlManager: SQLManager


    val pluginName: String
        get() = plugin.description.name

    override fun getPluginFolder(): File {
        return plugin.dataFolder
    }


    fun registerEvents(events: Listener) {
        ProxyServer.getInstance().pluginManager
            .registerListener(this, events)
    }

    fun registerCommand(comand: Command) {
        ProxyServer.getInstance().pluginManager.registerCommand(this, comand)
    }

    fun console(message: String) {
        ProxyServer.getInstance().console.sendMessage(TextComponent(message))
    }


    override fun getPlugin(): Plugin {
        return this
    }

    override fun getSystemName(): String {
        return description.name
    }


    fun log(message: String) {
        console("§b" + this.prefix + "§a" + message)
    }

    fun error(message: String) {
        console("§e" + this.prefix + "§c" + message)
    }


    override fun onLoad() {
        val loadingHybrids: HybridTypes = HybridTypes
        val currentInstance = this
        if (!currentInstance.started) {
            currentInstance.dbManager = DatabaseManager()
            currentInstance.config =(Config(currentInstance, "config.yml"))
            currentInstance.messages =(Config(currentInstance, "messages.yml"))
            currentInstance.storage=(Config(currentInstance, "storage.yml"))
            currentInstance.settings=(PluginSettings())
            currentInstance.config.add("settings", currentInstance.settings)
            currentInstance.config.add("database", currentInstance.dbManager)
            currentInstance.config.saveConfig()
            currentInstance.settings = currentInstance.config["settings", PluginSettings::class.java]
            currentInstance.dbManager= (currentInstance.config["database", DBManager::class.java])
            currentInstance.sqlManager= (SQLManager(currentInstance.dbManager))
            //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            //currentInstance.setStarted(true)

            // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.dbManager.isEnabled) {
                currentInstance.dbManager.openConnection()
            }
        }
    }

    fun onActivation() {
    }

    override fun onEnable() {
        if (!started) onLoad()
        resolvePut<EduardBungeePlugin>(this)
    }

    override fun onDisable() {
        resolveTake<EduardBungeePlugin>(this)
    }

    fun save() {
    }

    fun reload() {
    }

    fun configDefault() {
    }

    fun unregisterTasks() {
    }

    fun unregisterServices() {
    }

    fun unregisterListeners() {
    }

    fun unregisterCommands() {
    }

    fun unregisterStorableClasses() {
    }

    fun getBoolean(key: String): Boolean {
        return this.config.getBoolean(key)
    }

    fun getInt(key: String): Int {
        return this.config.getInt(key)
    }

    fun getDouble(key: String): Double {
        return this.config.getDouble(key)
    }

    fun message(key: String): String {
        return this.messages.message(key)
    }

    fun getMessages(key: String): MutableList<String> {
        return (this.messages.getMessages(key) ?: mutableListOf<String>()) as MutableList<String>
    }


    fun getString(key: String): String {
        return this.config.message(key)
    }
}
