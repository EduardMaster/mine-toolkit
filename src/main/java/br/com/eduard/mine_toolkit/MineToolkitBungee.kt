package br.com.eduard.mine_toolkit

import br.com.eduard.mine_toolkit.bungee.BungeeAPI
import br.com.eduard.mine_toolkit.bungee.ServerSpigot
import br.com.eduard.mine_toolkit.config.Config
import br.com.eduard.database.DBManager
import br.com.eduard.database.HybridTypes
import br.com.eduard.database.SQLManager
import br.com.eduard.mine_toolkit.hybrid.BungeeServer
import br.com.eduard.mine_toolkit.hybrid.Hybrid
import br.com.eduard.java_utils.Copyable
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_toolkit.commands.bungee.BungeeReloadCommand
import br.com.eduard.mine_toolkit.plugin.IPluginInstance
import br.com.eduard.mine_toolkit.plugin.PluginSettings
import br.com.eduard.storage.StorageAPI
import br.com.eduard.mine_toolkit.listeners.BungeePlugins
import br.com.eduard.mine_toolkit.tasks.BungeeDatabaseUpdaterTask
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Locale.getDefault
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


@Suppress("deprecated")
class MineToolkitBungee(private val plugin: Plugin) : IPluginInstance<Plugin> {

    companion object {
        lateinit var instance: MineToolkitBungee

        init {
            Hybrid.instance = BungeeServer
        }
    }

    init {
        instance = this
    }


    var started = false
    lateinit var configs: Config
    lateinit var storage: Config
    lateinit var messages: Config
    lateinit var dbManager: DBManager
    lateinit var sqlManager: SQLManager
    lateinit var settings: PluginSettings

    val pluginName: String
        get() = plugin.description.name

    val dataFolder get() = plugin.dataFolder

    override fun getPluginFolder(): File {
        return plugin.dataFolder
    }

    fun log(message: String) {
        console("§f$message")
    }

    fun console(message: String) {
        ProxyServer.getInstance().console
            .sendMessage(TextComponent("§b[MineToolkit]§r $message"))
    }

    fun error(message: String) {
        console("§c$message")
    }

    fun onLoad() {
        HybridTypes.register()
        StorageAPI.setDebug(false)
        val currentInstance: MineToolkitBungee = this
        if (!currentInstance.started) {
            currentInstance.dbManager = DBManager()
            currentInstance.configs = Config(currentInstance, "config.yml")
            currentInstance.messages = Config(currentInstance, "messages.yml")
            currentInstance.storage = Config(currentInstance, "storage.yml")
            currentInstance.settings = PluginSettings()
            currentInstance.configs.add("settings", currentInstance.settings)
            currentInstance.configs.add("database", currentInstance.dbManager)
            currentInstance.configs.saveConfig()
            currentInstance.settings = currentInstance.configs.get("settings", PluginSettings::class.java)
            currentInstance.dbManager = currentInstance.configs.get("database", DBManager::class.java)
            currentInstance.sqlManager = SQLManager(currentInstance.dbManager)
            //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            currentInstance.started = true
            // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.dbManager.isEnabled) {
                currentInstance.dbManager.openConnection()
            }
        }


    }

    fun getBoolean(key: String) = configs.getBoolean(key)
    fun reload() {
        log("Inicio do Recarregamento do MineToolkit")
        configs.reloadConfig()
        messages.reloadConfig()
        if (getBoolean("bungee-api")) {
            BungeeAPI.bungee.register(plugin)
        }
        configDefault()
        log("Ativando debug de sistemas caso marcado na config como 'true'")
        StorageAPI.setDebug(configs.getBoolean("debug.storage"))
        DBManager.setDebug(configs.getBoolean("debug.database"))
        Copyable.setDebug(configs.getBoolean("debug.copyable"))

        try {
            log("Carregando formato de dinheiro da config")
            Extra.MONEY = DecimalFormat(
                configs.getString("money-format"),
                DecimalFormatSymbols.getInstance(Locale.forLanguageTag(configs.getString("money-format-locale")))
            )
            log("Formato valido")
        } catch (exception: Exception) {
            error("Formato do dinheiro invalido " + configs.getString("money-format"))
        }

        mysqlDownload()
        log("Recarregamento do MineToolkit concluido.")
    }


    fun mysqlDownload() {

        if (!dbManager.hasConnection()) return
        log("SQL Conectado iniciando modifications")
        sqlManager.createTable(ServerSpigot::class.java)

        if (!getBoolean("bungee-api")) return
        for (server in sqlManager.getAll<ServerSpigot>()) {
            BungeeAPI.servers[server.name.lowercase(getDefault())] = server
        }
        for (server in ProxyServer.getInstance().servers.values) {
            val spigot = BungeeAPI.servers[server.name.lowercase(getDefault())]
            if (spigot == null) {
                val servidor = BungeeAPI.getServer(server.name)
                servidor.host = server.address.hostName
                servidor.port = server.address.port
                servidor.players = server.players
                    .map { it.name }
                servidor.count = server.players.size
                sqlManager.insertData(servidor)
            }
        }
    }

    fun onEnable() {
        StorageAPI.setDebug(false)

        reload()

        ProxyServer.getInstance().pluginManager
            .registerCommand(plugin, BungeeReloadCommand())

        ProxyServer.getInstance().scheduler.schedule(
            plugin,
            BungeeDatabaseUpdaterTask(),
            1, 1, TimeUnit.SECONDS
        );

        ProxyServer.getInstance().scheduler.schedule(
            plugin, BungeePlugins(),
            1, 1, TimeUnit.SECONDS
        );

        asyncSQLUpdater()
    }

    lateinit var databaseUpdater: Thread
    fun asyncSQLUpdater() {
        databaseUpdater = thread {
            val updater = BungeeDatabaseUpdaterTask()
            while (true) {
                updater.run()
                try {
                    Thread.sleep(50)
                } catch (ex: Exception) {
                    break
                }
            }
        }
    }

    fun configDefault() {
        configs.add("bungee-api", true)
        configs.add("debug.storage", false)
        configs.add("debug.copyable", false)
        configs.add("debug.commands", false)
        configs.add("debug.replacers", false)
        configs.add("debug.database", false)
        configs.add("money-format", "###,###.##")
        configs.add("money-format-locale", "PT-BR")
        configs.saveConfig()

    }

    fun save() {

    }


    fun onDisable() {
        if (getBoolean("bungee-api")) {
            BungeeAPI.bungee.unregister()
        }
        dbManager.closeConnection()

        databaseUpdater.interrupt()

    }

    fun unregisterTasks() {

    }

    fun unregisterListeners() {

    }

    fun unregisterServices() {

    }

    fun unregisterCommands() {

    }

    override fun getPlugin(): Plugin {
        return plugin
    }

    override fun getSystemName(): String {
        return pluginName;
    }


}