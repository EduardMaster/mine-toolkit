package br.com.eduard.mine_toolkit.bungee

import br.com.eduard.eduardapi.EduardAPI
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.ObjectInputStream
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.IOException
import java.lang.ClassNotFoundException

class BukkitMessageListener(var bukkitController: BukkitController) : PluginMessageListener {
    fun readMessage(arrayIn: ByteArrayInputStream, data: DataInputStream) {
        try {
            val objStream = ObjectInputStream(arrayIn)
            val server = objStream.readUTF()
            val tag = objStream.readUTF()
            if (tag == "server-update") {
                val spigot = objStream.readObject() as ServerSpigot
                BungeeAPI.servers[spigot.name.toLowerCase()] = spigot
            } else {
                val line = data.readUTF()
                bukkitController.receiveMessage(server, tag, line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
        }
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel == BungeeAPI.channel) {
            val arrayIn = ByteArrayInputStream(message)
            val data = DataInputStream(arrayIn)
            EduardAPI.instance.asyncTask{
                readMessage(arrayIn, data)
            }
        }
    }

}