package br.com.eduard.mine_toolkit.server

import org.bukkit.entity.Player

interface MachineSystem : PluginSystem{

    fun unstallMachinesOfAtPlot(player : Player)
}