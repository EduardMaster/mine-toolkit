package br.com.eduard.mine_toolkit.commands

import br.com.eduard.mine_toolkit.game.SoundEffect
import br.com.eduard.mine_toolkit.manager.CommandManager
import br.com.eduard.mine_utils.Mine

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GotoCommand : CommandManager("goto", "ir", "irpara") {

    init {
        description = "Teleporta para outro mundo"
        usage = "/<command> <world>"
    }

    /**
     * Som para o Teleporte
     */
    var OPT_SOUND_TELEPORT = SoundEffect.create("ENDERMAN_TELEPORT")
    var message = "§6Voce foi teleportado para o Mundo §e\$world"
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) return false
        if (!Mine.onlyPlayer(sender)) return true
        val player = sender as Player
        if (Mine.existsWorld(sender, args[0])) {
            val world = Bukkit.getWorld(args[0])
            Mine.teleport(player, world!!.spawnLocation)
            OPT_SOUND_TELEPORT.create(player)
            Mine.send(player, message.replace("\$world", world.name))
        }

        return true
    }
}