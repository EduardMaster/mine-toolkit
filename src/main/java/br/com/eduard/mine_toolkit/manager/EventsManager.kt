package br.com.eduard.mine_toolkit.manager

import br.com.eduard.mine_toolkit.plugin.IPluginInstance
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
/**
 * A base class designed to simplify the lifecycle management of Bukkit [Listener] instances.
 *
 * This manager provides automated methods to register and unregister events, ensuring that
 * listeners are correctly bound to their parent plugin and cleaned up when necessary.
 *
 * @author Eduard
 * @version 1.0
 */
open class EventsManager : Listener {
    /**
     * Indicates whether the [Listener] is currently registered with the Bukkit PluginManager.
     */
    @Transient
    var isRegistered: Boolean = false

    /**
     * The [JavaPlugin] instance responsible for managing this listener.
     */
    @Transient
    var plugin: JavaPlugin = defaultPlugin()

    /**
     * Automatically discovers the plugin instance that provided this class.
     * * @return The [JavaPlugin] associated with this class.
     */
    private fun defaultPlugin(): JavaPlugin {
        return JavaPlugin.getProvidingPlugin(javaClass)
    }

    /**
     * Registers the listener using a custom plugin instance wrapper.
     * * @param plugin The [IPluginInstance] wrapper containing the [JavaPlugin].
     */
    open fun register(plugin: IPluginInstance<*>) {
        registerListener(plugin.plugin as JavaPlugin)
    }

    /**
     * Registers this class as an event listener in the Bukkit server.
     * * If the listener is already registered, it will be unregistered first to
     * prevent duplicate event handling.
     *
     * @param plugin The [JavaPlugin] instance used to register the events.
     */
    open fun registerListener(plugin: JavaPlugin) {
        unregisterListener()
        this.plugin = plugin
        this.isRegistered = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    /**
     * Unregisters the listener from all [HandlerList]s.
     * * This method effectively stops this class from receiving further Bukkit events.
     * If the listener is not currently registered, this call does nothing.
     */
    fun unregisterListener() {
        if (!isRegistered) return
        HandlerList.unregisterAll(this)
        this.isRegistered = false
    }
}