package br.com.eduard.mine_toolkit.command

import br.com.eduard.mine_toolkit.hybrid.Hybrid
import br.com.eduard.java_utils.Extra
import br.com.eduard.mine_toolkit.plugin.IPluginInstance
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.Locale
import java.util.Locale.getDefault
import java.lang.Runnable

class BukkitCommand(val command: br.com.eduard.mine_toolkit.command.Command)
    : Command(command.name),
    CommandExecutor,
    TabCompleter, HybridCommand {


    override fun register(plugin: IPluginInstance) {
        register(plugin.plugin as JavaPlugin)
    }
    override fun unregister(plugin: IPluginInstance){
        unregister(plugin.plugin as JavaPlugin)
    }
    /**
     * @return O Hashmap onde estão registrados todos comandos e aliases
     */
    private fun getServerCommandsMap(): CommandMap? {
        try {

            return Extra.getFieldValue(Bukkit.getServer().pluginManager, "commandMap") as CommandMap
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
    /**
     * @return O HashMap onde estão registrados todos comandos e aliases
     */
    private fun getServerCommandsHashMap(): MutableMap<String, Command>? {
        try {
            val simpleCommandMap = getServerCommandsMap()
            val field = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
            field.isAccessible = true
            return field.get(simpleCommandMap) as MutableMap<String, Command>
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun unregister(plugin: JavaPlugin){
        try {

            val commandMap = getServerCommandsMap()?:return
            unregister(commandMap)
            //commandMap.getCommand("cmd").unregister(commandMap)
            val commandsMap =  getServerCommandsHashMap()!!
            val cmdName = name.lowercase(getDefault())
            val pluginName = plugin.name.lowercase(getDefault())
            for (aliase in aliases) {
                //log("Removendo aliase §a$aliase§f do comando §b$cmdName")
                commandsMap.remove(aliase.lowercase(getDefault()))
                commandsMap.remove(pluginName.lowercase(getDefault()) + ":" + aliase.lowercase(getDefault()))
            }
            try {
                commandsMap.remove(cmdName)
                commandsMap.remove(pluginName.lowercase(getDefault()) + ":" + cmdName)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun register(plugin: JavaPlugin) {
        var cmd: Command? = plugin.getCommand(command.name)
        var registred = false
        if ((cmd != null)) {
            registred = true
            (cmd as PluginCommand).setExecutor(this)
            cmd.tabCompleter = this
        } else cmd = this

        cmd.permission = command.permission
        cmd.permissionMessage = command.permissionMessage
        cmd.aliases = command.aliases
        cmd.usage = command.usage
        cmd.description = command.description

        if (!registred) {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val serverClass = Extra.getClassFrom(Bukkit.getServer())
                val field = serverClass.getDeclaredField("commandMap")
                field.isAccessible = true
                val map = field[Bukkit.getServer()] as CommandMap
                map.register(plugin.name, cmd)
            });
        }
    }


    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {
            command.processCommand(
                Hybrid.instance.getPlayer(sender.name, sender.uniqueId),
                args.toList()
            )
        } else {
            command.processCommand(Hybrid.instance.console, args.toList())
        }

        return true
    }

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        try {

            if (sender.hasPermission(permission!!)) {
                return onCommand(sender, this, label, args)
            } else {
                sender.sendMessage(permissionMessage)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String> {

        return onTabComplete(sender, this, alias, args)
    }



    override fun onTabComplete(
        sender: CommandSender, cmd: Command,
        label: String,
        args: Array<String>
    ): List<String> {
        val result = command.processTabComplete(
            if (sender is Player)
                Hybrid.instance.getPlayer(sender.name, sender.uniqueId)
            else Hybrid.instance.console, args.toList()
        )
        return result
    }
}