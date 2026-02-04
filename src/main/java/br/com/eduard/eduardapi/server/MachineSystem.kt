package br.com.eduard.eduardapi.server

import org.bukkit.entity.Player

interface MachineSystem : PluginSystem{

    fun unstallMachinesOfAtPlot(player : Player)
}