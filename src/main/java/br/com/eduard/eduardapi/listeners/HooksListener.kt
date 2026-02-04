package br.com.eduard.eduardapi.listeners

import br.com.eduard.eduardapi.server.PluginHook
import br.com.eduard.mine_toolkit.manager.EventsManager
import org.bukkit.event.EventHandler
import org.bukkit.event.server.PluginEnableEvent

class HooksListener : EventsManager() {

    @EventHandler
    fun onPluginEnableEvent(event: PluginEnableEvent) {
        val rooks = PluginHook.getRooks(event.plugin.name)
        rooks.forEach(PluginHook::onPluginActive)
    }

}