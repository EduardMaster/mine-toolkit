package br.com.eduard.mine_toolkit.command

import br.com.eduard.mine_toolkit.plugin.IPluginInstance

interface HybridCommand {
    fun register(plugin : IPluginInstance<*>)
    fun unregister(plugin : IPluginInstance<*>)

}