package br.com.eduard.mine_toolkit

import br.com.eduard.database.BukkitTypes
import br.com.eduard.mine_utils.BukkitBungeeAPI
import br.com.eduard.mine_utils.BukkitTimeHandler
import br.com.eduard.java_utils.Copyable
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_utils.Mine
import br.com.eduard.mine_utils.MineReflect
import br.com.eduard.mine_utils.VaultAPI
import br.com.eduard.mine_toolkit.commands.api.ApiCommand
import br.com.eduard.mine_toolkit.commands.map.MapCommand
import br.com.eduard.mine_toolkit.commands.performance.PerformanceCommand
import br.com.eduard.mine_toolkit.core.BukkitInfoGenerator

import br.com.eduard.mine_toolkit.bungee.BungeeAPI
import br.com.eduard.mine_toolkit.bungee.ServerSpigot
import br.com.eduard.mine_toolkit.config.Config
import br.com.eduard.mine_toolkit.config.ConfigSection
import br.com.eduard.database.DBManager
import br.com.eduard.database.HybridTypes
import br.com.eduard.database.SQLManager
import br.com.eduard.mine_toolkit.commands.EnchantCommand
import br.com.eduard.mine_toolkit.commands.GotoCommand
import br.com.eduard.mine_toolkit.commands.RunCommand
import br.com.eduard.mine_toolkit.commands.SetSkinCommand
import br.com.eduard.mine_toolkit.commands.SetXPCommand
import br.com.eduard.mine_toolkit.commands.SoundCommand
import br.com.eduard.mine_toolkit.core.BukkitReplacers
import br.com.eduard.mine_toolkit.core.PlayerSkin
import br.com.eduard.mine_toolkit.hybrid.BukkitServer
import br.com.eduard.mine_toolkit.hybrid.Hybrid
import br.com.eduard.mine_toolkit.kotlin.store
import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_toolkit.menu.Menu
import br.com.eduard.mine_toolkit.menu.MenuButton
import br.com.eduard.mine_toolkit.menu.Product
import br.com.eduard.mine_toolkit.menu.Shop
import br.com.eduard.mine_toolkit.menu.Slot
import br.com.eduard.mine_toolkit.plugin.IPluginInstance
import br.com.eduard.mine_toolkit.plugin.PluginSettings
import br.com.eduard.mine_toolkit.score.DisplayBoard
import br.com.eduard.storage.StorageAPI
import br.com.eduard.storage.storables.BukkitStorables
import br.com.eduard.mine_toolkit.listeners.BukkitPlugins
import br.com.eduard.mine_toolkit.listeners.EduWorldEditListener
import br.com.eduard.mine_toolkit.listeners.MineToolkitListener
import br.com.eduard.mine_toolkit.listeners.HooksListener
import br.com.eduard.mine_toolkit.tasks.AutoSaveAndBackupTask
import br.com.eduard.mine_toolkit.tasks.DatabaseUpdaterTask
import br.com.eduard.mine_toolkit.tasks.MenuAutoUpdaterTask
import br.com.eduard.mine_toolkit.tasks.PlayerTargetPlayerTask
import br.com.eduard.mine_toolkit.minigame.MinigameSchematic

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Locale.getDefault

/**
 * Core class of MineToolkit on Bukkit
 *
 * @author Eduard
 * @version 1.3
 * @since 0.5
 */
class MineToolkit(private val plugin: JavaPlugin) : BukkitTimeHandler, IPluginInstance {
    fun getString(key: String) = configs.getString(key)
    fun message(key: String) = messages.message(key)
    var started = false
    var configs = Config(plugin, "config.yml")
    var storage = Config(plugin, "storage.yml")
    var messages = Config(plugin, "messages.yml")
    lateinit var settings: PluginSettings
    lateinit var dbManager: DBManager
    lateinit var sqlManager: SQLManager
    lateinit var databaseUpdaterThread : DatabaseUpdaterTask
    fun onLoad() {
        HybridTypes.register()
        StorageAPI.setDebug(false)
        instance = this
        val currentInstance: MineToolkit = this
        if (!currentInstance.started) {
            currentInstance.dbManager = DBManager()
            currentInstance.configs = Config(currentInstance, "config.yml")
            currentInstance.messages = Config(currentInstance, "messages.yml")
            currentInstance.storage = Config(currentInstance, "storage.yml")
            currentInstance.settings = PluginSettings()
            currentInstance.configs.add("settings", currentInstance.settings)
            currentInstance.configs.add("database", currentInstance.dbManager)
            currentInstance.configs.saveConfig()
            currentInstance.settings = currentInstance.configs["settings", PluginSettings::class.java]
            currentInstance.dbManager = currentInstance.configs["database", DBManager::class.java]
            currentInstance.sqlManager = SQLManager(currentInstance.dbManager)
            //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            currentInstance.started = true
            // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.dbManager.isEnabled) {
                currentInstance.dbManager.openConnection()
            }
        }
        BukkitTypes.register()


    }

    fun getPluginName(): String {
       return  plugin.description.name
    }

    fun getDataFolder() : File = plugin.dataFolder

    override fun getPluginFolder(): File {
        return plugin.dataFolder
    }


    fun log(message: String) {
        console("§f$message")
    }

    fun console(message: String) {
        Bukkit.getConsoleSender().sendMessage("§b[MineToolkit]§r $message")
    }

    fun error(message: String) {
        console("§c$message")
    }

    fun storage() {
        StorageAPI.setDebug(configs.getBoolean("debug-storage"))
        log("Registrando classes da MineToolkit")
        //StorageAPI.registerPackage(javaClass, "net.eduard.api.lib")
        store<Product>()
        store<MenuButton>()
        store<Shop>()
        store<Menu>()
        store<Slot>()
        //StorageAPI.registerPackage(Minigame::class.java)
        BukkitStorables.load()
        StorageAPI.startGson()
        log("Storables do Bukkit carregado!")
    }


    fun onEnable() {
        if (!started) {
            this.onLoad()
        }
        MinigameSchematic.MAPS_FOLDER = File(instance.getPluginFolder(), "maps/")
        storage()
        VaultAPI.setupVault()
        BukkitBungeeAPI.register(plugin)
        BukkitBungeeAPI.requestCurrentServer()
        BungeeAPI.bukkit.register(plugin)
        reload()
        commands()
        events()
        tasks()
        loadServers()
        log("§aCarregado com sucesso!")

    }


    fun resetScoreboards() {
        for (teams in Mine.getMainScoreboard().teams) {
            teams.unregister()
        }
        for (objective in Mine.getMainScoreboard().objectives) {
            objective.unregister()
        }
        for (player in Mine.getPlayers()) {
            player.scoreboard = Mine.getMainScoreboard()
            player.maxHealth = 20.0
            player.health = 20.0
            player.isHealthScaled = false
        }
    }

    fun getBoolean(key: String) = configs.getBoolean(key);
    fun tasks() {
        resetScoreboards()
        log("Scoreboards dos jogadores online resetadas!")

        BukkitReplacers()
        log("Replacers ativados")

        log("Ativando Sistema atualizador de menus abertos")

        if (getBoolean("menu-updater.enabled"))
            MenuAutoUpdaterTask().asyncTimer()

        BukkitInfoGenerator(this)
        log("Base de dados de Enums do Bukkit gerado com sucesso")

        log("Carregando Moedas")
       // store<CurrencyJHCash>()
        //store<CurrencyVaultEconomy>()
       // CurrencyManager.register(CurrencyVaultEconomy())
       // JHCashHook()
        log("Moedas carregadas")

        log("Ativando tasks (Timers)")
        // Na versão 1.16 precisa ser em Sync não pode ser Async
        PlayerTargetPlayerTask().asyncTimer()
        AutoSaveAndBackupTask().asyncTimer()
        databaseUpdaterThread = DatabaseUpdaterTask()
        databaseUpdaterThread.start()
        log("Tasks ativados com sucesso")
    }

    fun events() {
        log("Ativando listeners dos Eventos")
        MineToolkitListener().register(this)
        HooksListener().register(this)
        EduWorldEditListener().register(this)

        BukkitPlugins().register(this)
        log("Listeners dos Eventos ativados com sucesso")
    }

    fun commands() {
        log("Ativando comandos")
        ApiCommand().register()
        MapCommand().register()
        EnchantCommand().registerCommand(plugin)
        GotoCommand().registerCommand(plugin)
        SoundCommand().registerCommand(plugin)
        SetXPCommand().registerCommand(plugin)
        SetSkinCommand().registerCommand(plugin)
        PerformanceCommand().registerCommand(plugin)
        RunCommand().registerCommand(plugin)
        log("Comandos ativados com sucesso")
    }

    fun loadServers() {
        if (sqlManager.hasConnection()) {
            sqlManager.cacheInfo()
            log("Carregando infos dos servidores")
            sqlManager.createTable(ServerSpigot::class.java)
            for (server in sqlManager.getAll<ServerSpigot>()) {
                BungeeAPI.servers[server.name.lowercase(getDefault())] = server
            }
        }
    }


    fun reload() {
        log("Inicio do Recarregamento do MineToolkit")
        configs.reloadConfig()
        messages.reloadConfig()
        configDefault()
        log("Ativando debug de sistemas caso marcado na config como 'true'")
        StorageAPI.setDebug(configs.getBoolean("debug.storage"))
        DBManager.setDebug(configs.getBoolean("debug.database"))
        Config.isDebug = configs.getBoolean("debug.config")
        ConfigSection.isDebug = configs.getBoolean("debug.config-section")
        Menu.isDebug = configs.getBoolean("debug.menu")
        //Holographic.debug = configs.getBoolean("debug.holograms")
        CommandManager.debugEnabled = configs.getBoolean("debug.commands")
        Copyable.setDebug(configs.getBoolean("debug.copyable"))
        BukkitBungeeAPI.setDebuging(configs.getBoolean("debug.bungee-bukkit"))
        Mine.OPT_DEBUG_REPLACERS = configs.getBoolean("debug.replacers")
        PlayerSkin.reloadSkins()
        MineReflect.LORE_ITEM_STACK = configs.message("stack-design")
        loadMaps()

        configs.saveConfig()

        try {
            log("Carregando formatador de dinheiro")
            val format = configs.getString("money.format")
            val locale = DecimalFormatSymbols.getInstance(Locale.forLanguageTag(configs.getString("money.locale")))
            Extra.MONEY = DecimalFormat(format, locale)
            log("Formatador de dinheiro: $format")
            log("Locale do Formatador de dinheiro: $locale")
            log("Formatando numero 1m: " + Extra.MONEY.format(1000000))
            log("Formatando numero 1000,50: " + Extra.MONEY.format(1000.500))

        } catch (exception: Exception) {
            error("Formato do dinheiro invalido " + configs.getString("money.format"))
            exception.printStackTrace()
        }

        DisplayBoard.colorFix = configs.getBoolean("scoreboard.color-fix")
        DisplayBoard.nameLimit = configs.getInt("scoreboard.name-limit")
        DisplayBoard.prefixLimit = configs.getInt("scoreboard.prefix-limit")
        DisplayBoard.suffixLimit = configs.getInt("scoreboard.suffix-limit")

        log("Recarregamento do MineToolkit concluido.")

    }


    fun configDefault() {
        configs.add("debug.config", false)
        configs.add("debug.config-section", false)
        configs.add("debug.bungee-bukkit", false)
        configs.add("debug.storage", false)
        configs.add("debug.copyable", false)
        configs.add("debug.commands", false)
        configs.add("debug.replacers", false)
        configs.add("debug.database", false)
        configs.add("debug.holograms", false)
        configs.add("debug.menu", false)
        configs.add("menu-updater.enabled", true)
        configs.add("menu-updater.ticks", 20L)
        configs.add("features.block-mine-event", true)
        configs.add("features.show-plugins", false)
        configs.add("features.skins", false)
        configs.add("features.custom-skin", "Eduard")
        configs.add("features.auto-respawn", true)
        configs.add("scoreboard.name-limit", 40)
        configs.add("scoreboard.prefix-limit", 16)
        configs.add("scoreboard.suffix-limit", 16)
        configs.add("scoreboard.color-fix", true)
        configs.add("async-license-check", false);
        configs.add("stack-design", "§aQuantidade: §f%stack")
        configs.add("money.format", "###,###.##")
        configs.add("money.locale", "PT-BR")
        configs.saveConfig()
    }

    fun save() {

    }


    fun onDisable() {

        PlayerSkin.saveSkins()
        saveMaps()
        log("desativado com sucesso!")
        BungeeAPI.controller.unregister()
        unregisterCommands()
        unregisterTasks()
        unregisterListeners()
        unregisterServices()
        MinigameSchematic.unloadAll()
        sqlManager.dbManager.closeConnection()
        databaseUpdaterThread.interrupt()
    }

    fun unregisterTasks() {

    }

    fun unregisterListeners() {

    }

    fun unregisterServices() {

    }

    fun unregisterCommands() {
        val commands = CommandManager.commandsRegistred.values.toList()
        for (cmd in commands) {
            val name = cmd.name
            CommandManager.log("Comando $name desregistrado")
            cmd.unregisterCommand()
            cmd.unregisterListener()
        }
        CommandManager.commandsRegistred.clear()
    }


    override fun getPlugin(): Plugin {
        return this.plugin
    }

    override fun getSystemName(): String {
        return getPluginName();
    }

    companion object {
        lateinit var instance: MineToolkit

        init {
            Hybrid.instance = BukkitServer
        }
        /*
        Som do rosnar do gato
        private val ROSNAR = SoundEffect.create("CAT_PURR")
        private val VALUE_TNT_POWER = 4f
        private val VALUE_CREEPER_POWER = 3f
        private val VALUE_WALKING_VELOCITY = -0.08f
        private val DAY_IN_HOUR = 24
        private val DAY_IN_MINUTES = DAY_IN_HOUR * 60
        private val DAY_IN_SECONDS = DAY_IN_MINUTES * 60
        private val DAY_IN_TICKS = (DAY_IN_SECONDS * 20).toLong()
        private val DAY_IN_MILLIS = DAY_IN_TICKS * 50
         */
        fun loadMaps() {
            MinigameSchematic.loadAll(MinigameSchematic.MAPS_FOLDER)
            instance.log("Mapas carregados!")
        }
        /**
         * Salva todos os mapas no sistema de armazenamento
         */
        fun saveMaps() {
            MinigameSchematic.saveAll(MinigameSchematic.MAPS_FOLDER)
            instance.log("Mapas salvados!")
        }
    }

    override fun getPluginConnected(): Plugin {
        return plugin
    }

}
