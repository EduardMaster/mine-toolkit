package br.com.eduard.mine_toolkit.server

interface ChatSystem :  PluginSystem {

    fun isMuted(playerName : String) : Boolean
    fun mute(playerName: String)
    fun getPlayersMuted() : List<String>

}